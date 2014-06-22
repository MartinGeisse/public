/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.operator;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractComputeExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;

/**
 * Applies a {@link CastOperator} to a sub-expression.
 */
public final class CastExpression extends AbstractComputeExpression {

	/**
	 * the operator
	 */
	private final CastOperator operator;

	/**
	 * the operand
	 */
	private final Expression operand;

	/**
	 * Constructor.
	 * @param operator the operator
	 * @param operand the operand
	 */
	public CastExpression(CastOperator operator, Expression operand) {
		this.operator = operator;
		this.operand = operand;
	}

	/**
	 * Getter method for the operator.
	 * @return the operator
	 */
	public CastOperator getOperator() {
		return operator;
	}
	
	/**
	 * Getter method for the operand.
	 * @return the operand
	 */
	public Expression getOperand() {
		return operand;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		return operator.apply(operand.evaluate(environment));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("((");
		dumper.print(operator.getTypeName());
		dumper.print(")(");
		operand.dump(dumper);
		dumper.print("))");
	}

}
