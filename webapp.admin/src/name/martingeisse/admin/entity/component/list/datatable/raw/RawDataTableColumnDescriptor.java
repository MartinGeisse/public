/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.raw;

import name.martingeisse.admin.entity.component.list.datatable.DataTableColumnDescriptor;
import name.martingeisse.common.util.ParameterUtil;

/**
 * Specialized column descriptor class for {@link RawEntityListPanel}.
 */
public class RawDataTableColumnDescriptor extends DataTableColumnDescriptor {

	/**
	 * the fieldName
	 */
	private final String fieldName;

	/**
	 * Constructor.
	 * @param title the title of this column
	 * @param fieldName the name of the field to display in this column
	 */
	public RawDataTableColumnDescriptor(final String title, final String fieldName) {
		super(title);
		ParameterUtil.ensureNotNull(fieldName, "fieldName");
		this.fieldName = fieldName;
	}

	/**
	 * Getter method for the fieldName.
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

}
