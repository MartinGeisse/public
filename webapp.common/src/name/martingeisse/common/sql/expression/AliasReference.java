/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * Represents a reference to a column alias or computation alias.
 */
public final class AliasReference implements IExpression {

	/**
	 * the alias
	 */
	private final String alias;

	/**
	 * Constructor.
	 * @param alias the alias to refer to
	 */
	public AliasReference(final String alias) {
		this.alias = alias;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.expression.IExpression#writeTo(name.martingeisse.common.sql.build.ISqlBuilder)
	 */
	@Override
	public void writeTo(ISqlBuilder builder) {
		builder.writeColumnAliasReference(alias);
	}

}
