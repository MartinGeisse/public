/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.database.QueryConditions;

import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;

/**
 * Utility class to build a condition predicate for an
 * entity to fetch.
 */
public class EntityConditions extends QueryConditions {

	/**
	 * Constructor using the default entity path for the specified entity.
	 * @param entity the entity
	 */
	public EntityConditions(final EntityDescriptor entity) {
		this(entity.getQueryBuilder().getDefaultPath(), Ops.AND);
	}

	/**
	 * Constructor.
	 * @param entityPath the path to the entity to build a predicate for
	 */
	public EntityConditions(final Path<?> entityPath) {
		this(entityPath, Ops.AND);
	}

	/**
	 * Constructor using the default entity path.
	 * @param entity the entity
	 * @param buildOperator the operator used to combine expressions
	 */
	public EntityConditions(final EntityDescriptor entity, final Operator<Boolean> buildOperator) {
		this(entity.getQueryBuilder().getDefaultPath(), buildOperator);
	}

	/**
	 * Constructor.
	 * @param entityPath the path to the entity to build a predicate for
	 * @param buildOperator the operator used to combine expressions
	 */
	public EntityConditions(final Path<?> entityPath, final Operator<Boolean> buildOperator) {
		super(entityPath, buildOperator);
	}

}
