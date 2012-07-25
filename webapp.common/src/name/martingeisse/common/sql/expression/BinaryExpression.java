/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * Binary operator expression, e.g. ADD or EQUAL.
 */
public final class BinaryExpression implements IExpression {

	/**
	 * the leftOperand
	 */
	private final IExpression leftOperand;

	/**
	 * the operator
	 */
	private final BinaryOperator operator;

	/**
	 * the rightOperand
	 */
	private final IExpression rightOperand;

	/**
	 * Constructor.
	 * @param leftOperand the left operand
	 * @param operator the operator
	 * @param rightOperand the right operand
	 */
	public BinaryExpression(final IExpression leftOperand, final BinaryOperator operator, final IExpression rightOperand) {
		this.leftOperand = leftOperand;
		this.operator = operator;
		this.rightOperand = rightOperand;
	}

	/**
	 * Getter method for the leftOperand.
	 * @return the leftOperand
	 */
	public IExpression getLeftOperand() {
		return leftOperand;
	}
	
	/**
	 * Getter method for the operator.
	 * @return the operator
	 */
	public BinaryOperator getOperator() {
		return operator;
	}
	
	/**
	 * Getter method for the rightOperand.
	 * @return the rightOperand
	 */
	public IExpression getRightOperand() {
		return rightOperand;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.expression.IExpression#writeTo(name.martingeisse.common.sql.build.ISqlBuilder)
	 */
	@Override
	public void writeTo(ISqlBuilder builder) {
		builder.write(" (");
		leftOperand.writeTo(builder);
		builder.write(" ");
		builder.write(operator.getSymbol());
		builder.write(" ");
		rightOperand.writeTo(builder);
		builder.write(") ");
	}

}
