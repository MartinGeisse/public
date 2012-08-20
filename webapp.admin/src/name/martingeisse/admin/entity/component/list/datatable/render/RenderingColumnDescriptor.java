/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.render;

import name.martingeisse.admin.entity.component.list.datatable.DataTableColumnDescriptor;

import com.mysema.query.types.Expression;

/**
 * Column descriptor class for JSON-rendering data tables.
 * 
 * This class adds a "sort expression", i.e. a QueryDSL expression that
 * is used to sort rows.
 */
public class RenderingColumnDescriptor extends DataTableColumnDescriptor {

	/**
	 * the sortExpression
	 */
	private Expression<Comparable<?>> sortExpression;

	/**
	 * Constructor.
	 */
	public RenderingColumnDescriptor() {
	}

	/**
	 * Constructor.
	 * @param title the title
	 */
	public RenderingColumnDescriptor(final String title) {
		super(title);
	}

	/**
	 * Constructor.
	 * @param title the title
	 * @param sortExpression the expression used to sort rows
	 */
	public RenderingColumnDescriptor(final String title, final Expression<Comparable<?>> sortExpression) {
		super(title);
		this.sortExpression = sortExpression;
	}

	/**
	 * Getter method for the sortExpression.
	 * @return the sortExpression
	 */
	public Expression<Comparable<?>> getSortExpression() {
		return sortExpression;
	}

	/**
	 * Setter method for the sortExpression.
	 * @param sortExpression the sortExpression to set
	 */
	public void setSortExpression(final Expression<Comparable<?>> sortExpression) {
		this.sortExpression = sortExpression;
	}

}
