/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

/**
 * This class contains a nested table result, i.e. the result for
 * a nested table query.
 */
public class NestedTableResult {

	/**
	 * the rootTable
	 */
	private final NestedTable rootTable;

	/**
	 * Constructor.
	 * @param rootTable the root table
	 */
	public NestedTableResult(NestedTable rootTable) {
		this.rootTable = rootTable;
	}
	
	/**
	 * Getter method for the rootTable.
	 * @return the rootTable
	 */
	public NestedTable getRootTable() {
		return rootTable;
	}

	/**
	 * Dumps this table to stdout for debugging.
	 */
	public void dump() {
		System.out.println("- begin NestedTableResult");
		rootTable.dump();
		System.out.println("- end NestedTableResult");
	}

}
