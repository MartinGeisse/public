/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

import java.io.Serializable;
import java.util.List;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * Specialization of {@link AbstractQuerydslSingleResultTableBasedCacheRegion}
 * that skips the transformation and just stores the table row beans directly
 * in the cache.
 * 
 * @param <K> the type of cache keys
 * @param <R> the table row bean type (which is also the type of cached values)
 */
public abstract class AbstractQuerydslSingleResultTableCacheRegion<K extends Serializable, R> extends AbstractQuerydslSingleResultTableBasedCacheRegion<K, R, R> {

	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param additionalPredicates additional predicates (if any)
	 */
	public AbstractQuerydslSingleResultTableCacheRegion(final String regionName, RelationalPath<R> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
		super(regionName, path, keyExpression, additionalPredicates);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractQuerydslSingleResultTableBasedCacheRegion#transformValue(java.io.Serializable, java.lang.Object)
	 */
	@Override
	protected R transformValue(K key, R row) {
		return row;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractQuerydslSingleResultTableBasedCacheRegion#transformValues(java.util.List, java.util.List)
	 */
	@Override
	protected List<R> transformValues(List<K> keys, List<R> rows) {
		return rows;
	}
	
}