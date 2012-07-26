/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import name.martingeisse.common.sql.expression.IExpression;

/**
 * This class implements a general-purpose entity filter.
 */
public class EntityListFilter implements IEntityListFilter {

	/**
	 * the filterExpression
	 */
	private IExpression filterExpression;

	/**
	 * Constructor.
	 */
	public EntityListFilter() {
	}

	/**
	 * Constructor.
	 * @param filterExpression the filter expression
	 */
	public EntityListFilter(final IExpression filterExpression) {
		this.filterExpression = filterExpression;
	}

	/**
	 * Getter method for the filterExpression.
	 * @return the filterExpression
	 */
	@Override
	public IExpression getFilterExpression() {
		return filterExpression;
	}

	/**
	 * Setter method for the filterExpression.
	 * @param filterExpression the filterExpression to set
	 */
	public void setFilterExpression(final IExpression filterExpression) {
		this.filterExpression = filterExpression;
	}

}
