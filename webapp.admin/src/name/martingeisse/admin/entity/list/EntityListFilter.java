/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import java.io.Serializable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * This class implements a general-purpose entity filter.
 */
public class EntityListFilter implements IEntityListFilter, Serializable {

	/**
	 * the entityExpression
	 */
	private Expression<?> entityExpression;
	
	/**
	 * the filterPredicate
	 */
	private Predicate filterPredicate;

	/**
	 * Constructor.
	 */
	public EntityListFilter() {
	}

	/**
	 * Constructor.
	 * @param entityExpression the expression that represents the entity
	 * @param filterPredicate the filter predicate
	 */
	public EntityListFilter(Expression<?> entityExpression, Predicate filterPredicate) {
		this.entityExpression = entityExpression;
		this.filterPredicate = filterPredicate;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.list.IEntityListFilter#getEntityExpression()
	 */
	@Override
	public Expression<?> getEntityExpression() {
		return entityExpression;
	}
	
	/**
	 * Setter method for the entityExpression.
	 * @param entityExpression the entityExpression to set
	 */
	public void setEntityExpression(Expression<?> entityExpression) {
		this.entityExpression = entityExpression;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.list.IEntityListFilter#getFilterPredicate()
	 */
	@Override
	public Predicate getFilterPredicate() {
		return filterPredicate;
	}

	/**
	 * Setter method for the filterPredicate.
	 * @param filterPredicate the filterPredicate to set
	 */
	public void setFilterPredicate(Predicate filterPredicate) {
		this.filterPredicate = filterPredicate;
	}

}
