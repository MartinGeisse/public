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
import com.mysema.query.types.Predicate;
import com.mysema.query.types.PredicateOperation;

/**
 * This cache implementation automatically fetches values using
 * QueryDSL queries. The whole region represents a database table;
 * cached values are sets of database rows which are optionally
 * transformed via a method provided by the concrete subclass.
 * See also {@link TODO} for a
 * simplified non-transformed version, and
 * {@link AbstractQuerydslListResultTableBasedCacheRegion} for a 
 * version based on (ordered) lists.
 * 
 * Apart from the {@link RelationalPath} that specifies the table to fetch
 * from, fetching is based on a key expression and a set of additional predicates.
 * The value fetched for the cache key 'cacheKey' is the set of rows that
 * satisfy the condition:
 * 
 *   ((cacheKey == keyExpression) && additionalPredicates)
 * 
 * For example, to cache sets of non-deleted users by home city, the
 * keyExpression would be the QueryDSL path for the city column and
 * the additionalPredicates would be (NOT deleted).
 * 
 * This class is abstract since it cannot connect to a database in a generic
 * way; subclasses must provide a mechanism to create a fresh QueryDSL
 * {@link SQLQuery} (this object wraps a JDBC {@link Connection}).
 * Subclasses must also provide a transformation from sets of database row
 * beans to cached values. Use {@link TODO} to
 * skip that transformation.
 * 
 * @param <K> the type of cache keys
 * @param <R> the table row bean type
 * @param <V> the type of cached values
 */
public abstract class AbstractQuerydslSetResultTableBasedCacheRegion<K extends Serializable, R, V> extends AbstractCacheRegion<K, V> {

	TODO
	
	EVTL. NUR LIST-VERSION, KEINE SET-VERSION (EINFACHER) !?
	
	TODO

	/**
	 * the path
	 */
	private RelationalPath<R> path;
	
	/**
	 * the keyExpression
	 */
	private Expression<?> keyExpression;
	
	/**
	 * the additionalPredicates
	 */
	private Predicate[] additionalPredicates;
	
	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param additionalPredicates additional predicates (if any)
	 */
	public AbstractQuerydslSetResultTableBasedCacheRegion(final String regionName, RelationalPath<R> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
		super(regionName);
		this.path = ParameterUtil.ensureNotNull(path, "path");
		this.keyExpression = ParameterUtil.ensureNotNull(keyExpression, "keyExpression");
		this.additionalPredicates = ParameterUtil.ensureNotNull(additionalPredicates, "additionalPredicates");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractCacheRegion#fetch(java.io.Serializable)
	 */
	@Override
	protected V fetch(K key) throws NoSuchElementException {
		// TODO
		ParameterUtil.ensureNotNull(key, "key");
		SQLQuery query = ReturnValueUtil.nullNotAllowed(createQuery(), "createQuery()");
		query.from(path).where(new PredicateOperation(Ops.EQ, keyExpression, Expressions.constant(key)));
		query.where(additionalPredicates).limit(1);
		return transformValue(query.singleResult(path));
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractCacheRegion#fetchMultiple(java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected List<V> fetchMultiple(List<K> keys) throws UnsupportedOperationException {
		// TODO
		
		// build the query
		ParameterUtil.ensureNotNull(keys, "keys");
		ParameterUtil.ensureNoNullElement(keys, "keys");
		SQLQuery query = ReturnValueUtil.nullNotAllowed(createQuery(), "createQuery()");
		query.from(path).where(new PredicateOperation(Ops.IN, keyExpression, Expressions.constant(keys)));
		query.where(additionalPredicates);
		
		// fetch results and store them in a map, indexed by value of the key expression
		CloseableIterator<Object[]> iterator = query.iterate(keyExpression, path);
		Map<K, R> foundValues = new HashMap<K, R>();
		while (iterator.hasNext()) {
			Object[] entry = iterator.next();
			foundValues.put((K)entry[0], (R)entry[1]);
		}
		iterator.close();

		// create the result list, using null for missing keys
		List<R> preTransformationResult = new ArrayList<R>();
		for (K key : keys) {
			preTransformationResult.add(foundValues.get(key));
		}
		
		// transform the result list
		return transformValues(preTransformationResult);
		
	}

	/**
	 * @return a new {@link SQLQuery}
	 */
	protected abstract SQLQuery createQuery();
	
	/**
	 * Transforms a single row.
	 * 
	 * @param row the row to transform (may be null if no row was found)
	 * @return the value (may be null to store a null value in the cache)
	 */
	protected abstract V transformValue(R row);

	/**
	 * Transforms multiple rows. This method provides a default implementation that
	 * invokes {@link #transformValue(Object)} on each element. Subclasses are encouraged
	 * to provide a more efficient implementation where possible.
	 * 
	 * @param rows the rows to transform. The list itself is never null, but may contain null elements
	 * whenever no row was found for a key. This method can assume that the argument list is not used
	 * by the caller after this method is invoked, so the list may, for example, be modified or
	 * re-used for the result list.
	 * @return the values. The list must not be null but may contain null elements to store null
	 * values in the cache.
	 */
	protected List<V> transformValues(List<R> rows) {
		List<V> result = new ArrayList<V>();
		for (R row : rows) {
			result.add(transformValue(row));
		}
		return result;
	}
	
}
