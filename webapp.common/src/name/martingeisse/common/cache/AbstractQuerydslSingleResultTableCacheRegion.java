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
 * Specialization of {@link AbstractQuerydslTableBasedCacheRegion} that skips the
 * transformation and just stores the table row beans directly in the cache.
 * 
 * @param <K> the type of cache keys
 * @param <V> the type of cached values
 */
public abstract class AbstractQuerydslSingleResultTableCacheRegion<K extends Serializable, V> extends AbstractQuerydslSingleResultTableBasedCacheRegion<K, V, V> {

	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param additionalPredicates additional predicates (if any)
	 */
	public AbstractQuerydslSingleResultTableCacheRegion(final String regionName, RelationalPath<V> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
		super(regionName, path, keyExpression, additionalPredicates);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractQuerydslTableBasedCacheRegion#transformValue(java.lang.Object)
	 */
	@Override
	protected V transformValue(V row) {
		return row;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractQuerydslTableBasedCacheRegion#transformValues(java.util.List)
	 */
	@Override
	protected List<V> transformValues(List<V> rows) {
		return rows;
	}
	
}
