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
import com.mysema.query.types.Predicate;
import com.mysema.query.types.PredicateOperation;

/**
 * This cache implementation automatically fetches values using
 * QueryDSL queries. The whole region represents a database table;
 * cached values are database rows which are optionally transformed
 * via a method provided by the concrete subclass. See also
 * {@link AbstractQuerydslSingleResultTableCacheRegion} for a
 * simplified, non-transformed version.
 * 
 * Apart from the {@link RelationalPath} that specifies the table to fetch
 * from, fetching is based on a key expression and a set of additional predicates.
 * The value fetched for the cache key 'cacheKey' is a single row that
 * satisfies the condition:
 * 
 *   ((cacheKey == keyExpression) && additionalPredicates)
 * 
 * A typical case would use the table ID (or any other unique column) as the
 * keyExpression and, if applicable, some "not marked as deleted" condition
 * as the additional predicates.
 * 
 * This class is abstract since it cannot connect to a database in a generic
 * way; subclasses must provide a mechanism to create a fresh QueryDSL
 * {@link SQLQuery} (this object wraps a JDBC {@link Connection}).
 * Subclasses must also provide a transformation from database row beans
 * to cached values. Use {@link AbstractQuerydslSingleResultTableCacheRegion}
 * to skip that transformation.
 * 
 * @param <K> the type of cache keys
 * @param <R> the table row bean type
 * @param <V> the type of cached values
 */
public abstract class AbstractQuerydslSingleResultTableBasedCacheRegion<K extends Serializable, R, V> extends AbstractCacheRegion<K, V> {

	/**
	 * the path
	 */
	private final RelationalPath<R> path;
	
	/**
	 * the keyExpression
	 */
	private final Expression<?> keyExpression;
	
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
	public AbstractQuerydslSingleResultTableBasedCacheRegion(final String regionName, RelationalPath<R> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
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
		ParameterUtil.ensureNotNull(key, "key");
		SQLQuery query = ReturnValueUtil.nullNotAllowed(createQuery(), "createQuery()");
		query.from(path).where(new PredicateOperation(Ops.EQ, keyExpression, Expressions.constant(key)));
		query.where(additionalPredicates).limit(1);
		return transformValue(key, query.singleResult(path));
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
		return transformValues(keys, preTransformationResult);
		
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
	protected abstract V transformValue(K key, R row);

	/**
	 * Transforms multiple rows. This method provides a default implementation that
	 * invokes {@link #transformValue(Serializable, Object)} on each element. Subclasses are encouraged
	 * to provide a more efficient implementation where possible.
	 * 
	 * @param rows the rows to transform. The list itself is never null, but may contain null elements
	 * whenever no row was found for a key. This method can assume that the argument list is not used
	 * by the caller after this method is invoked, so the list may, for example, be modified or
	 * re-used for the result list.
	 * @return the values. The list must not be null but may contain null elements to store null
	 * values in the cache.
	 */
	protected List<V> transformValues(List<K> keys, List<R> rows) {
		List<V> result = new ArrayList<V>();
		Iterator<K> keyIterator = keys.iterator();
		Iterator<R> rowIterator = rows.iterator();
		while (keyIterator.hasNext()) {
			result.add(transformValue(keyIterator.next(), rowIterator.next()));
		}
		return result;
	}
	
}