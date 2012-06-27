/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.definition.nestedtable.NestedTableResult;

/**
 * A renderable nested table.
 */
public class NestedTable extends AbstractFigureItem {

	/**
	 * the data
	 */
	private NestedTableResult data;

	/**
	 * Constructor.
	 */
	public NestedTable() {
	}

	/**
	 * Constructor.
	 * @param data the nested-table data
	 */
	public NestedTable(final NestedTableResult data) {
		this.data = data;
	}

	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public NestedTableResult getData() {
		return data;
	}

	/**
	 * Setter method for the data.
	 * @param data the data to set
	 */
	public void setData(final NestedTableResult data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public NestedTable bindToData(DataSources dataSources) {
		return this;
	}

}
