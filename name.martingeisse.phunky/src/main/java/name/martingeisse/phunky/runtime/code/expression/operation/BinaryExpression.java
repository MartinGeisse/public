/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.operation;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractComputeExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;

/**
 * Combines two sub-expressions using a {@link BinaryOperator}.
 */
public final class BinaryExpression extends AbstractComputeExpression {

	/**
	 * the leftHandSide
	 */
	private final Expression leftHandSide;

	/**
	 * the operator
	 */
	private final BinaryOperator operator;

	/**
	 * the rightHandSide
	 */
	private final Expression rightHandSide;

	/**
	 * Constructor.
	 * @param leftHandSide the left-hand side
	 * @param operator the operator
	 * @param rightHandSide the right-hand side
	 */
	public BinaryExpression(Expression leftHandSide, BinaryOperator operator, Expression rightHandSide) {
		this.leftHandSide = leftHandSide;
		this.operator = operator;
		this.rightHandSide = rightHandSide;
	}

	/**
	 * Getter method for the leftHandSide.
	 * @return the leftHandSide
	 */
	public Expression getLeftHandSide() {
		return leftHandSide;
	}

	/**
	 * Getter method for the operator.
	 * @return the operator
	 */
	public BinaryOperator getOperator() {
		return operator;
	}

	/**
	 * Getter method for the rightHandSide.
	 * @return the rightHandSide
	 */
	public Expression getRightHandSide() {
		return rightHandSide;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		return operator.applyToExpressions(environment, leftHandSide, rightHandSide);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print('(');
		leftHandSide.dump(dumper);
		dumper.print(' ');
		dumper.print(operator.getSymbol());
		dumper.print(' ');
		rightHandSide.dump(dumper);
		dumper.print(')');
	}
	
}
