/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.common.computation.predicate.IPredicate;
import name.martingeisse.common.sql.expression.IExpression;

/**
 * This filter allows to filter lists of entity instances.
 */
public interface IEntityListFilter extends IPredicate<EntityInstance> {

	/**
	 * The alias used for the entity table.
	 */
	public static final String ALIAS = "t";
	
	/**
	 * Creates an {@link IExpression} for the SQL expression that does the
	 * filtering on the database side.
	 * @return the filter expression
	 */
	public IExpression getFilterExpression();
	
}
