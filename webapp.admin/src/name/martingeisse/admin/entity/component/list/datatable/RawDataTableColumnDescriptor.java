/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable;

/**
 * Specialized column descriptor class for {@link RawEntityListPanel}.
 */
public class RawDataTableColumnDescriptor extends DataTableColumnDescriptor {

	/**
	 * the fieldName
	 */
	private String fieldName;

	/**
	 * Constructor.
	 */
	public RawDataTableColumnDescriptor() {
	}

	/**
	 * Constructor.
	 * @param title the title of this column
	 * @param fieldName the name of the field to display in this column
	 */
	public RawDataTableColumnDescriptor(final String title, final String fieldName) {
		super(title);
		this.fieldName = fieldName;
	}

	/**
	 * Getter method for the fieldName.
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Setter method for the fieldName.
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

}
