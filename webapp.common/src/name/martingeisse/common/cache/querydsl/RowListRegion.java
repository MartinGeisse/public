/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache.querydsl;

import java.io.Serializable;
import java.util.List;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

/**
 * Cache implementation for a multi-row, non-transformed cache.
 * 
 * @param <K> the type of cache keys
 * @param <R> the table row bean type (which is also the type of cached values)
 */
public class RowListRegion<K extends Serializable, R> extends AbstractRowListRegion<K, R, List<R>> {

	/**
	 * Constructor.
	 * @param regionName the name of the region represented by this object
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param additionalPredicates additional predicates (if any)
	 */
	public RowListRegion(final String regionName, RelationalPath<R> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
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
	public RowListRegion(final String regionName, RelationalPath<R> path, Expression<?> keyExpression, OrderSpecifier<?> orderSpecifier, Predicate... additionalPredicates) {
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
	public RowListRegion(String regionName, RelationalPath<R> path, Expression<?> keyExpression, OrderSpecifier<?>[] orderSpecifiers, Predicate... additionalPredicates) {
		super(regionName, path, keyExpression, orderSpecifiers, additionalPredicates);
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
