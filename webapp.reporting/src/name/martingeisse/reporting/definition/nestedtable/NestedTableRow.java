/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a row node in a {@link NestedTableResult}.
 */
public class NestedTableRow {

	/**
	 * the values
	 */
	private List<String> values;

	/**
	 * the subtables
	 */
	private List<NestedTable> subtables;

	/**
	 * Constructor.
	 */
	public NestedTableRow() {
		this.values = new ArrayList<String>();
		this.subtables = new ArrayList<NestedTable>();
	}

	/**
	 * Getter method for the values.
	 * @return the values
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * Setter method for the values.
	 * @param values the values to set
	 */
	public void setValues(final List<String> values) {
		this.values = values;
	}

	/**
	 * Getter method for the subtables.
	 * @return the subtables
	 */
	public List<NestedTable> getSubtables() {
		return subtables;
	}

	/**
	 * Setter method for the subtables.
	 * @param subtables the subtables to set
	 */
	public void setSubtables(final List<NestedTable> subtables) {
		this.subtables = subtables;
	}

	/**
	 * Dumps this table to stdout for debugging.
	 */
	public void dump() {
		System.out.print("- begin NestedTableRow: ");
		for (final String value : values) {
			System.out.print(value);
			System.out.print(", ");
		}
		System.out.println();
		for (final NestedTable subtable : subtables) {
			subtable.dump();
		}
		System.out.println("- end NestedTableRow");
	}

}
