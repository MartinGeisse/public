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
public class NestedTableResult implements INestedTableResult {

	/**
	 * the rootTable
	 */
	private final NestedTableTable rootTable;
	
	/**
	 * the cursor
	 */
	private final NestedTableRowsCursor cursor;

	/**
	 * Constructor.
	 * @param rootTable the root table
	 */
	public NestedTableResult(NestedTableTable rootTable) {
		this.rootTable = rootTable;
		this.cursor = new NestedTableRowsCursor();
		this.cursor.initialize(rootTable.getRows().iterator());
	}
	
	/**
	 * Getter method for the rootTable.
	 * @return the rootTable
	 */
	public NestedTableTable getRootTable() {
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

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.nestedtable.INestedTableResult#getRootTableTitle()
	 */
	@Override
	public String getRootTableTitle() {
		return rootTable.getTitle();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.nestedtable.INestedTableResult#getRowsCursor()
	 */
	@Override
	public INestedTableRowsCursor getRowsCursor() {
		return cursor;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.nestedtable.INestedTableResult#makeExplicitOnDemand()
	 */
	@Override
	public NestedTableResult makeExplicitOnDemand() {
		return this;
	}

}
