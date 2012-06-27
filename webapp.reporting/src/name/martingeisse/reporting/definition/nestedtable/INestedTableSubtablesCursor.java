/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

/**
 * This interface loops through the subtables of an {@link INestedTableQuery}
 * row node.
 */
public interface INestedTableSubtablesCursor {

	/**
	 * Moves this cursor to the next subtable. This method invalidates the sub-cursors
	 * obtained from this cursor, their sub-cursors, and so on.
	 * @return true if successful, false if no subtable is left
	 */
	public boolean nextSubtable();
	
	/**
	 * Returns the title of the current subtable.
	 * @return the title
	 */
	public String getTitle();
	
	/**
	 * Returns the rows cursor. This cursor returns the rows for the
	 * current subtable and cannot be rewound. When the parent cursor is moved to the
	 * next subtable, any remaining rows not returned by the rows cursor
	 * are skipped.
	 * @return the rows cursor
	 */
	public INestedTableRowsCursor getRows();
	
}
