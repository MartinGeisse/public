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

	/**
	 * Creates an expression chain for an arbitrary number of sub-expressions.
	 * 
	 * Returns null if the expressions argument contains no expressions, and the
	 * one and only expression if it contains a single one. Otherwise combines
	 * the expressions in a left-associative way using the specified operator.
	 * 
	 * @param expressions the expressions
	 * @param operator the operator to use
	 * @return the chained expression
	 */
	public static IExpression createChain(Iterable<IExpression> expressions, BinaryOperator operator) {
		IExpression result = null;
		for (IExpression subExpression : expressions) {
			if (result == null) {
				result = subExpression;
			} else {
				result = new BinaryExpression(result, operator, subExpression);
			}
		}
		return result;
	}

	/**
	 * AND-combines the specified expressions. This uses createChain() with the AND
	 * operator and TRUE as the default value for an empty expression list.
	 * @param expressions the expressions
	 * @return the chained expression
	 */
	public static IExpression createAndChain(Iterable<IExpression> expressions) {
		IExpression result = createChain(expressions, BinaryOperator.AND);
		return (result == null ? BooleanLiteral.TRUE : result);
	}

	/**
	 * OR-combines the specified expressions. This uses createChain() with the OR
	 * operator and FALSE as the default value for an empty expression list.
	 * @param expressions the expressions
	 * @return the chained expression
	 */
	public static IExpression createOrChain(Iterable<IExpression> expressions) {
		IExpression result = createChain(expressions, BinaryOperator.OR);
		return (result == null ? BooleanLiteral.FALSE : result);
	}

	/**
	 * ADD-combines the specified expressions, i.e. creates a sum expression.
	 * This uses createChain() with the ADD operator and 0 as the default
	 * value for an empty expression list.
	 * @param expressions the expressions
	 * @return the chained expression
	 */
	public static IExpression createAddChain(Iterable<IExpression> expressions) {
		IExpression result = createChain(expressions, BinaryOperator.ADD);
		return (result == null ? new IntegerLiteral(0) : result);
	}

	/**
	 * MULTIPLY-combines the specified expressions, i.e. creates a product expression.
	 * This uses createChain() with the MULTIPLY operator and 1 as the default
	 * value for an empty expression list.
	 * @param expressions the expressions
	 * @return the chained expression
	 */
	public static IExpression createMultiplyChain(Iterable<IExpression> expressions) {
		IExpression result = createChain(expressions, BinaryOperator.MULTIPLY);
		return (result == null ? new IntegerLiteral(1) : result);
	}
	
}
