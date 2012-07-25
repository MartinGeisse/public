/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * Integer literal expression.
 */
public final class IntegerLiteral implements IExpression {
	
	/**
	 * the value
	 */
	private final int value;
	
	/**
	 * Constructor.
	 * @param value the value of this literal
	 */
	public IntegerLiteral(int value) {
		this.value = value;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.expression.IExpression#writeTo(name.martingeisse.common.sql.build.ISqlBuilder)
	 */
	@Override
	public void writeTo(ISqlBuilder builder) {
		builder.write(" " + value + " ");
	}

}
