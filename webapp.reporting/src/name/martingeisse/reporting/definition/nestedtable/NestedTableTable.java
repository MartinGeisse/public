/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a table node in a {@link NestedTableResult}.
 */
public class NestedTableTable {

	/**
	 * the title
	 */
	private String title;

	/**
	 * the columnNames
	 */
	private List<String> columnNames;

	/**
	 * the rows
	 */
	private List<NestedTableRow> rows;

	/**
	 * Constructor.
	 */
	public NestedTableTable() {
		this.title = null;
		this.columnNames = new ArrayList<String>();
		this.rows = new ArrayList<NestedTableRow>();
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

	/**
	 * Getter method for the columnNames.
	 * @return the columnNames
	 */
	public List<String> getColumnNames() {
		return columnNames;
	}

	/**
	 * Setter method for the columnNames.
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(final List<String> columnNames) {
		this.columnNames = columnNames;
	}

	/**
	 * Getter method for the rows.
	 * @return the rows
	 */
	public List<NestedTableRow> getRows() {
		return rows;
	}

	/**
	 * Setter method for the rows.
	 * @param rows the rows to set
	 */
	public void setRows(final List<NestedTableRow> rows) {
		this.rows = rows;
	}

	/**
	 * Finds a column by name and returns its index.
	 * @param name the column name
	 * @return the column index, or -1 if not found
	 */
	public int findColumn(String name) {
		int i = 0;
		for (String columnName : columnNames) {
			if (columnName.equals(name)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	/**
	 * Dumps this table to stdout for debugging.
	 */
	public void dump() {
		System.out.println("- begin NestedTable: " + title);
		for (final NestedTableRow row : rows) {
			row.dump();
		}
		System.out.println("- end NestedTable");
	}

}
