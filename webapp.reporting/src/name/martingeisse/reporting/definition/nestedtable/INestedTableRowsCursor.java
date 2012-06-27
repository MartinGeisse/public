/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

/**
 * This interface loops through the rows of an {@link INestedTableQuery}
 * table node.
 */
public interface INestedTableRowsCursor {

	/**
	 * Moves this cursor to the next row. This method invalidates the sub-cursors
	 * obtained from this cursor, their sub-cursors, and so on.
	 * @return true if successful, false if no row is left
	 */
	public boolean nextRow();
	
	/**
	 * Returns the values stored in the current row.
	 * @return thevalues
	 */
	public String[] getValues();
	
	/**
	 * Returns the subtables cursor. This cursor returns the subtables for the
	 * current row and cannot be rewound. When the parent cursor is moved to the
	 * next row, any remaining subtables not returned by the subtable cursor
	 * are skipped.
	 * @return the subtable cursor
	 */
	public INestedTableSubtablesCursor getSubtables();
	
}
