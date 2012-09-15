/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable;

import name.martingeisse.common.util.ParameterUtil;

/**
 * DataTable implementations use this class to indicate column
 * behavior to the base class.
 */
public class DataTableColumnDescriptor {

	/**
	 * the title
	 */
	private final String title;

	/**
	 * Constructor.
	 * @param title the title
	 */
	public DataTableColumnDescriptor(String title) {
		ParameterUtil.ensureNotNull(title, "title");
		this.title = title;
	}

	/**
	 * Getter method for the title.
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

}
