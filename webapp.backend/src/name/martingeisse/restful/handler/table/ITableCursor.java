/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.handler.table;

import name.martingeisse.restful.type.IFieldType;

/**
 * This interface represents the current state of a query
 * for an {@link ITableDataProvider}.
 * 
 * The structure of the table -- that is, the field types
 * associated with columns -- is considered global information
 * of the query, not associated with any particular rows.
 * 
 * The rows, in contrast, are provided one after another,
 * with no way to "return" to previous rows. (Subclasses may
 * provide methods to do so, but in general it is not possible.)
 */
public interface ITableCursor {

	/**
	 * @return the column types
	 */
	public IFieldType[] getColumnTypes();

	/**
	 * Fetches the next row.
	 * @return the data values of the row, or null if all rows
	 * have been fetched.
	 */
	public Object[] fetchRow();
	
	/**
	 * Disposes of any resources associated with this cursor.
	 */
	public void close();
	
}
