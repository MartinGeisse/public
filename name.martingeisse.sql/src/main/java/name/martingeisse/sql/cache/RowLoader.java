/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.sql.cache;

import name.martingeisse.common.util.Wrapper;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * Cache loader implementation for a single-row, non-transformed cache.
 * 
 * Note that rows actually *are* transformed to a {@link Wrapper} because
 * Guava caches do not allow null values (which do occur for missing rows).
 * 
 * @param <K> the type of cache keys
 * @param <R> the table row bean type (a wrapper of which is also the type of cached values)
 */
public class RowLoader<K, R> extends AbstractRowLoader<K, R, Wrapper<R>> {

	/**
	 * Constructor.
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param additionalPredicates additional predicates (if any)
	 */
	public RowLoader(RelationalPath<R> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
		super(path, keyExpression, additionalPredicates);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractQuerydslSingleResultTableBasedCacheRegion#transformValue(java.io.Serializable, java.lang.Object)
	 */
	@Override
	protected Wrapper<R> transformValue(K key, R row) {
		return new Wrapper<R>(row);
	}
	
}
