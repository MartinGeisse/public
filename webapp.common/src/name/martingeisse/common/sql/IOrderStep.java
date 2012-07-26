/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * Represents a step (field or expression) in an ORDER BY clause.
 */
public interface IOrderStep {

	/**
	 * Writes this object to the specified SQL builder.
	 * @param builder the SQL builder
	 */
	public void writeTo(ISqlBuilder builder);
	
}
