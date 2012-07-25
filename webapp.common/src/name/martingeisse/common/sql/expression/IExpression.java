/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * An SQL expression.
 */
public interface IExpression {

	/**
	 * Writes this object to the specified SQL builder.
	 * @param builder the SQL builder
	 */
	public void writeTo(ISqlBuilder builder);

}
