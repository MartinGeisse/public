/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.reporting.datasource.DataSources;

/**
 * A renderable table.
 */
public class Table implements IBlockItem {

	/**
	 * the fieldNames
	 */
	private String[] fieldNames;

	/**
	 * the rows
	 */
	private List<String[]> rows;

	/**
	 * the caption
	 */
	private String caption;

	/**
	 * Constructor.
	 */
	public Table() {
		this.rows = new ArrayList<String[]>();
	}

	/**
	 * Getter method for the fieldNames.
	 * @return the fieldNames
	 */
	public String[] getFieldNames() {
		return fieldNames;
	}

	/**
	 * Setter method for the fieldNames.
	 * @param fieldNames the fieldNames to set
	 */
	public void setFieldNames(final String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	/**
	 * Getter method for the rows.
	 * @return the rows
	 */
	public List<String[]> getRows() {
		return rows;
	}

	/**
	 * Setter method for the rows.
	 * @param rows the rows to set
	 */
	public void setRows(final List<String[]> rows) {
		this.rows = rows;
	}

	/**
	 * Getter method for the caption.
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Setter method for the caption.
	 * @param caption the caption to set
	 */
	public void setCaption(final String caption) {
		this.caption = caption;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public Table bindToData(final DataSources dataSources) {
		return this;
	}

}
