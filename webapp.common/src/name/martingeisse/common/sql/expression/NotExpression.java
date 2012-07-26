/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * A logical negation expression.
 */
public final class NotExpression implements IExpression {

	/**
	 * the operand
	 */
	private final IExpression operand;
	
	/**
	 * Constructor.
	 * @param operand the operand to be negated
	 */
	public NotExpression(IExpression operand) {
		this.operand = operand;
	}
	
	/**
	 * Getter method for the operand.
	 * @return the operand
	 */
	public IExpression getOperand() {
		return operand;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.expression.IExpression#writeTo(name.martingeisse.common.sql.build.ISqlBuilder)
	 */
	@Override
	public void writeTo(ISqlBuilder builder) {
		builder.write(" (NOT ");
		operand.writeTo(builder);
		builder.write(" ) ");
	}

}
