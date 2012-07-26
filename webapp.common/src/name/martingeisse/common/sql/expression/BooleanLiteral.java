/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * Boolean literal expression. There are two singleton instances
 * for TRUE and FALSE.
 */
public final class BooleanLiteral implements IExpression {

	/**
	 * The singleton FALSE instance.
	 */
	public static final BooleanLiteral FALSE = new BooleanLiteral(false);

	/**
	 * The singleton TRUE instance.
	 */
	public static final BooleanLiteral TRUE = new BooleanLiteral(true);
	
	/**
	 * Returns the appropriate singleton instance of this class.
	 * @param value the value to represent
	 * @return the instance
	 */
	public static BooleanLiteral from(boolean value) {
		return (value ? TRUE : FALSE);
	}
	
	/**
	 * the value
	 */
	private final boolean value;
	
	/**
	 * Constructor.
	 */
	private BooleanLiteral(boolean value) {
		this.value = value;
	}
	
	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public boolean isValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.expression.IExpression#writeTo(name.martingeisse.common.sql.build.ISqlBuilder)
	 */
	@Override
	public void writeTo(ISqlBuilder builder) {
		builder.write(value ? " TRUE " : " FALSE ");
	}

}
