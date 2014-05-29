/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.sql.cache;

import java.io.Serializable;
import java.util.List;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

/**
 * Cache loader implementation for a multi-row, non-transformed cache.
 * 
 * @param <K> the type of cache keys
 * @param <R> the table row bean type (a list of which is also the type of cached values)
 */
public class RowListLoader<K extends Serializable, R> extends AbstractRowListLoader<K, R, List<R>> {

	/**
	 * Constructor.
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param additionalPredicates additional predicates (if any)
	 */
	public RowListLoader(RelationalPath<R> path, Expression<?> keyExpression, Predicate... additionalPredicates) {
		this(path, keyExpression, new OrderSpecifier<?>[0], additionalPredicates);
	}

	/**
	 * Constructor.
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param orderSpecifier the result order
	 * @param additionalPredicates additional predicates (if any)
	 */
	public RowListLoader(RelationalPath<R> path, Expression<?> keyExpression, OrderSpecifier<?> orderSpecifier, Predicate... additionalPredicates) {
		this(path, keyExpression, new OrderSpecifier<?>[] {orderSpecifier}, additionalPredicates);
	}

	/**
	 * Constructor.
	 * @param path the table path
	 * @param keyExpression the key expression
	 * @param orderSpecifiers the result order
	 * @param additionalPredicates additional predicates (if any)
	 */
	public RowListLoader(RelationalPath<R> path, Expression<?> keyExpression, OrderSpecifier<?>[] orderSpecifiers, Predicate... additionalPredicates) {
		super(path, keyExpression, orderSpecifiers, additionalPredicates);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.AbstractQuerydslListResultTableBasedCacheRegion#transformValue(java.io.Serializable, java.util.List)
	 */
	@Override
	protected List<R> transformValue(K key, List<R> rows) {
		return rows;
	}

}
