/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

import java.util.Iterator;

/**
 * Implementation of {@link INestedTableSubtablesCursor}.
 */
public final class NestedTableSubtableCursor implements INestedTableSubtablesCursor {

	/**
	 * the subtableIterator
	 */
	private Iterator<NestedTableTable> subtableIterator;

	/**
	 * the currentTable
	 */
	private NestedTableTable currentTable;

	/**
	 * the rowsCursor
	 */
	private NestedTableRowsCursor rowsCursor;

	/**
	 * Constructor.
	 */
	public NestedTableSubtableCursor() {
	}

	/**
	 * Initializes this cursor to return the subtables and rows from
	 * the specified iterator. The argument may be null to initialize
	 * to an empty set.
	 * @param subtableIterator the iterator to use, or null to clear
	 */
	public void initialize(Iterator<NestedTableTable> subtableIterator) {
		this.subtableIterator = subtableIterator;
		this.currentTable = null;
		initializeSubCursor();
	}
	
	/**
	 * If a current table is set, then the sub-cursor is initialized to iterate
	 * over the rows of that table. Otherwise it is initialized to the
	 * empty set.
	 * 
	 * This method is used to initialize the sub-cursor either when it
	 * is created or when its iterated set changes.
	 */
	private void initializeSubCursor() {
		if (rowsCursor != null) {
			rowsCursor.initialize(currentTable == null ? null : currentTable.getRows().iterator());
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.nestedtable.INestedTableSubtablesCursor#nextSubtable()
	 */
	@Override
	public boolean nextSubtable() {
		if (subtableIterator != null && subtableIterator.hasNext()) {
			currentTable = subtableIterator.next();
			initializeSubCursor();
			return true;
		} else {
			initialize(null);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.nestedtable.INestedTableSubtablesCursor#getTitle()
	 */
	@Override
	public String getTitle() {
		return (currentTable == null ? null : currentTable.getTitle());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.nestedtable.INestedTableSubtablesCursor#getRows()
	 */
	@Override
	public INestedTableRowsCursor getRows() {
		if (rowsCursor == null) {
			rowsCursor = new NestedTableRowsCursor();
			initializeSubCursor();
		}
		return rowsCursor;
	}

}
