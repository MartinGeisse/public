/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

/**
 * This interface wraps the whole result from an {@link INestedTableQuery}.
 */
public interface INestedTableResult {

	/**
	 * Returns the title of the root table.
	 * @return the root table title
	 */
	public String getRootTableTitle();

	/**
	 * Returns the rows cursor of the root table.
	 * @return the rows cursor
	 */
	public INestedTableRowsCursor getRowsCursor();

	/**
	 * Creates or returns a {@link NestedTableResult}, re-using data structures
	 * from this result as much as possible. Data structures that cannot be
	 * re-used are re-created as explicit data structures using the cursors from
	 * this result.
	 * 
	 * @return the explicit result
	 */
	public NestedTableResult makeExplicitOnDemand();
	
}
