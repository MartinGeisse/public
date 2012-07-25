/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * Represents what is being selected by a SELECT statement, i.e.
 * a column, aliased column, wildcard, etc.
 */
public interface ISelectTarget {

	/**
	 * Writes this object to the specified SQL builder.
	 * @param builder the SQL builder
	 */
	public void writeTo(ISqlBuilder builder);
	
}
