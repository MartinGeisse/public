/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.operator;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractComputeExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

/**
 * Applies a {@link UnaryOperator} to a sub-expression.
 */
public final class UnaryExpression extends AbstractComputeExpression {

	/**
	 * the operator
	 */
	private final UnaryOperator operator;

	/**
	 * the operand
	 */
	private final Expression operand;

	
	/**
	 * Constructor.
	 * @param operator the operator
	 * @param operand the operand
	 */
	public UnaryExpression(UnaryOperator operator, Expression operand) {
		this.operator = operator;
		this.operand = operand;
	}

	/**
	 * Getter method for the operator.
	 * @return the operator
	 */
	public UnaryOperator getOperator() {
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
		return operator.applyToExpression(environment, operand);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print('(');
		dumper.print(operator.getPrefixSymbol());
		operand.dump(dumper);
		dumper.print(operator.getPostfixSymbol());
		dumper.print(')');
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("unary");
		sub.property("operator").string(operator.name());
		operand.toJson(sub.property("operand"));
		sub.end();
	}

}
