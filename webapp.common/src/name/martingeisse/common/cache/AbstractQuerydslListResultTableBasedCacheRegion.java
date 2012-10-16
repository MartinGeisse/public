/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.PredicateOperation;

/**
 * This cache implementation automatically fetches values using
 * QueryDSL queries. The whole region represents a database table;
 * cached values are lists of database rows which are optionally
 * transformed via a method provided by the concrete subclass.
 * See also {@link AbstractQuerydslListResultTableCacheRegion} for a
 * simplified non-transformed version.
 * 
 * Apart from the {@link RelationalPath} that specifies the table to fetch
 * from, fetching is based on a key expression, a set of additional predicates,
 * and an optional order specifier. The value fetched for the cache key 'cacheKey'
 * is the list of rows (in the specified order, if any) that satisfy the condition:
 * 
 *   ((cacheKey == keyExpression) && additionalPredicates)
 * 
 * For example, to cache lists of non-deleted users by home city, the
 * keyExpression would be the QueryDSL path for the city column and
 * the additionalPredicates would be (NOT deleted).
 * 
 * This class is abstract since it cannot connect to a database in a generic
 * way; subclasses must provide a mechanism to create a fresh QueryDSL
 * {@link SQLQuery} (this object wraps a JDBC {@link Connection}).
 * Subclasses must also provide a transformation from lists of database row
 * beans to cached values. Use {@link AbstractQuerydslListResultTableCacheRegion} to
 * skip that transformation.
 * 
 * @param <K> the type of cache keys
 * @param <R> the table row bean type
 * @param <V> the type of cached values
 */
public abstract class AbstractQuerydslListResultTableBasedCacheRegion<K extends Serializable, R, V> extends AbstractCacheRegion<K, V> {

	/**
	 * the path
	 */
	private final RelationalPath<R> path;
	
	/**
	 * the keyExpression
	 */
	private final Expression<?> keyExpression;
	
	/**
	 * the orderSpecifiers
	 */
	private final OrderSpecifier<?>[] orderSpecifiers;
	
	/**
	 * the additionalPredicates
	 */
	private final Predicate[] additionalPredicates;
	
	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param additionalPredicates additional predicates (if any)
	 */
	public AbstractQuerydslListResultTableBasedCacheRegion(final String regionName, RelationalPath<R> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
		this(regionName, path, keyExpression, new OrderSpecifier<?>[0], additionalPredicates);
	}
	
	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param orderSpecifier the result order
	 * @param additionalPredicates additional predicates (if any)
	 */
	public AbstractQuerydslListResultTableBasedCacheRegion(final String regionName, RelationalPath<R> path, Expression<?> keyExpression, OrderSpecifier<?> orderSpecifier, Predicate... additionalPredicates) {
		this(regionName, path, keyExpression, new OrderSpecifier<?>[] {orderSpecifier}, additionalPredicates);
	}
	
	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param orderSpecifiers the result order
	 * @param additionalPredicates additional predicates (if any)
	 */
	public AbstractQuerydslListResultTableBasedCacheRegion(final String regionName, RelationalPath<R> path, Expression<?> keyExpression, OrderSpecifier<?>[] orderSpecifiers, Predicate... additionalPredicates) {
		super(regionName);
		this.path = ParameterUtil.ensureNotNull(path, "path");
		this.keyExpression = ParameterUtil.ensureNotNull(keyExpression, "keyExpression");
		this.orderSpecifiers = orderSpecifiers;
		this.additionalPredicates = ParameterUtil.ensureNotNull(additionalPredicates, "additionalPredicates");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractCacheRegion#fetch(java.io.Serializable)
	 */
	@Override
	protected V fetch(K key) throws NoSuchElementException {
		ParameterUtil.ensureNotNull(key, "key");
		SQLQuery query = ReturnValueUtil.nullNotAllowed(createQuery(), "createQuery()");
		query.from(path).where(new PredicateOperation(Ops.EQ, keyExpression, Expressions.constant(key)));
		query.where(additionalPredicates).orderBy(orderSpecifiers);
		return transformValue(key, query.list(path));
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractCacheRegion#fetchMultiple(java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected List<V> fetchMultiple(List<K> keys) throws UnsupportedOperationException {
		
		// build the query
		ParameterUtil.ensureNotNull(keys, "keys");
		ParameterUtil.ensureNoNullElement(keys, "keys");
		SQLQuery query = ReturnValueUtil.nullNotAllowed(createQuery(), "createQuery()");
		query.from(path).where(new PredicateOperation(Ops.IN, keyExpression, Expressions.constant(keys)));
		query.where(additionalPredicates).orderBy(orderSpecifiers);

		// fetch results and store them in a map, indexed by value of the key expression
		CloseableIterator<Object[]> iterator = query.iterate(keyExpression, path);
		Map<K, List<R>> foundValues = new HashMap<K, List<R>>();
		while (iterator.hasNext()) {
			Object[] entry = iterator.next();
			K key = (K)entry[0];
			R row = (R)entry[1];
			List<R> rowList = foundValues.get(key);
			if (rowList == null) {
				rowList = new ArrayList<R>();
				foundValues.put(key, rowList);
			}
			rowList.add(row);
		}
		iterator.close();

		// create the result list, using null for missing keys
		List<List<R>> preTransformationResult = new ArrayList<List<R>>();
		for (K key : keys) {
			List<R> foundList = foundValues.get(key);
			preTransformationResult.add(foundList == null ? new ArrayList<R>() : foundList);
		}
		
		// transform the result list
		return transformValues(keys, preTransformationResult);
		
	}

	/**
	 * @return a new {@link SQLQuery}
	 */
	protected abstract SQLQuery createQuery();
	
	/**
	 * Transforms a list of rows for a single key.
	 * 
	 * @param rows the row list to transform (empty if no row was found for that key; never null)
	 * @return the value (may be null to store a null value in the cache)
	 */
	protected abstract V transformValue(K key, List<R> rows);

	/**
	 * Transforms row lists for multiple keys. This method provides a default implementation that
	 * invokes {@link #transformValue(Serializable, List)} on each element. Subclasses are encouraged
	 * to provide a more efficient implementation where possible.
	 * 
	 * @param rowLists the row lists to transform. The list itself is never null, nor are its element
	 * lists null. Element lists may be empty whenever no row was found for a key. This method can
	 * assume that neither the argument list nor any of its element lists is used by the caller
	 * after this method is invoked, so the lists may, for example, be modified or re-used for the
	 * result list.
	 * @return the values. The list must not be null but may contain null elements to store null
	 * values in the cache.
	 */
	protected List<V> transformValues(List<K> keys, List<List<R>> rowLists) {
		List<V> result = new ArrayList<V>();
		Iterator<K> keyIterator = keys.iterator();
		Iterator<List<R>> rowListIterator = rowLists.iterator();
		while (keyIterator.hasNext()) {
			result.add(transformValue(keyIterator.next(), rowListIterator.next()));
		}
		return result;
	}
	
}