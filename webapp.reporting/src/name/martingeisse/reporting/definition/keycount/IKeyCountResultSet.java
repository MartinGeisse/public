/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.keycount;

/**
 * This class behaves similar to JDBC result sets, except for {@link KeyCountEntry}
 * objects instead of table rows.
 */
public interface IKeyCountResultSet {

	/**
	 * Fetches the next entry.
	 * @return true on success, false if no more entries are available
	 */
	public boolean next();
	
	/**
	 * Returns the current entry.
	 * @return the current entry
	 */
	public KeyCountEntry get();
	
	/**
	 * Closes the result set.
	 */
	public void close();
	
}
