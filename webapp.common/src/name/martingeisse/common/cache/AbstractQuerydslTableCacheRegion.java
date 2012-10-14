/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

import java.io.Serializable;
import java.sql.Connection;
import java.util.NoSuchElementException;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;

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
 * cached values are database rows.
 * 
 * Apart from the {@link RelationalPath} that specifies the table to fetch
 * from, fetching is based on a key expression and a set of additional predicates.
 * The value fetched for the cache key 'cacheKey' satisfies the condition:
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
 * 
 * @param <K> the type of cache keys
 * @param <V> the type of cached values
 */
public abstract class AbstractQuerydslTableCacheRegion<K extends Serializable, V> extends AbstractCacheRegion<K, V> {

	/**
	 * the path
	 */
	private RelationalPath<V> path;
	
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
	public AbstractQuerydslTableCacheRegion(final String regionName, RelationalPath<V> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
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
		return query.singleResult(path);
	}

	/**
	 * @return a new {@link SQLQuery}
	 */
	protected abstract SQLQuery createQuery();
	
}
