/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable;

/**
 * DataTable implementations use this class to indicate column
 * behavior to the base class.
 * 
 * TODO: parameter check / return value check: ab hier weiterprüfen
 */
public class DataTableColumnDescriptor {

	/**
	 * the title
	 */
	private String title;

	/**
	 * Constructor.
	 */
	public DataTableColumnDescriptor() {
	}

	/**
	 * Constructor.
	 * @param title the title
	 */
	public DataTableColumnDescriptor(String title) {
		this.title = title;
	}

	/**
	 * Getter method for the title.
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for the title.
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

}
