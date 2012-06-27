/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

import java.util.Iterator;

/**
 * Implementation of {@link INestedTableRowsCursor}.
 */
public class NestedTableRowsCursor implements INestedTableRowsCursor {

	/**
	 * the rowIterator
	 */
	private Iterator<NestedTableRow> rowIterator;

	/**
	 * the currentRow
	 */
	private NestedTableRow currentRow;

	/**
	 * the subtableCursor
	 */
	private NestedTableSubtableCursor subtableCursor;

	/**
	 * Constructor.
	 */
	public NestedTableRowsCursor() {
	}

	/**
	 * Initializes this cursor to return the rows and subtables from
	 * the specified iterator. The argument may be null to initialize
	 * to an empty set.
	 * @param rowIterator the iterator to use, or null to clear
	 */
	public void initialize(Iterator<NestedTableRow> rowIterator) {
		this.rowIterator = rowIterator;
		this.currentRow = null;
		initializeSubCursor();
	}
	
	/**
	 * If no sub-cursor exists, this method does nothing. Otherwise, f a
	 * current row is set, then the sub-cursor is initialized to iterate
	 * over the subtables of that row. Otherwise it is initialized to the
	 * empty set.
	 * 
	 * This method is used to initialize the sub-cursor either when it
	 * is created or when its iterated set changes.
	 */
	private void initializeSubCursor() {
		if (subtableCursor != null) {
			subtableCursor.initialize(currentRow == null ? null : currentRow.getSubtables().iterator());
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.nestedtable.INestedTableRowsCursor#nextRow()
	 */
	@Override
	public boolean nextRow() {
		if (rowIterator != null && rowIterator.hasNext()) {
			currentRow = rowIterator.next();
			initializeSubCursor();
			return true;
		} else {
			initialize(null);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.nestedtable.INestedTableRowsCursor#getValues()
	 */
	@Override
	public String[] getValues() {
		return (currentRow == null ? null : currentRow.getValues().toArray(new String[currentRow.getValues().size()]));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.nestedtable.INestedTableRowsCursor#getSubtables()
	 */
	@Override
	public INestedTableSubtablesCursor getSubtables() {
		if (subtableCursor == null) {
			subtableCursor = new NestedTableSubtableCursor();
			initializeSubCursor();
		}
		return subtableCursor;
	}

}
