/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * This filter allows to filter lists of entity instances.
 */
public interface IEntityListFilter {

	/**
	 * @return the expression that represents the entity
	 */
	public Expression<?> getEntityExpression();
	
	/**
	 * Creates an {@link Expression} for the SQL expression that does the
	 * filtering on the database side.
	 * @return the filter expression
	 */
	public Predicate getFilterPredicate();

}
