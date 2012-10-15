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
 * Specialization of {@link AbstractQuerydslListResultTableBasedCacheRegion} that skips the
 * transformation and just stores the lists of table row beans directly in the cache.
 * 
 * @param <K> the type of cache keys
 * @param <R> the table row bean type
 */
public abstract class AbstractQuerydslListResultTableCacheRegion<K extends Serializable, R> extends AbstractQuerydslListResultTableBasedCacheRegion<K, R, List<R>> {

	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param additionalPredicates additional predicates (if any)
	 */
	public AbstractQuerydslListResultTableCacheRegion(final String regionName, RelationalPath<R> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
		super(regionName, path, keyExpression, additionalPredicates);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractQuerydslListResultTableBasedCacheRegion#transformValue(java.io.Serializable, java.util.List)
	 */
	@Override
	protected List<R> transformValue(K key, List<R> rows) {
		return rows;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractQuerydslListResultTableBasedCacheRegion#transformValues(java.util.List, java.util.List)
	 */
	@Override
	protected List<List<R>> transformValues(List<K> keys, List<List<R>> rowLists) {
		return rowLists;
	}
	
}
