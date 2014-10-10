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
import name.martingeisse.phunky.runtime.variable.ValueAcceptor;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * This kind of expression is similar to a {@link BinaryExpression}, but the
 * left-hand side must be a variable. Evaluation first resolves that variable.
 * It then combines the variable's value with the right-hand side using the 
 * oeprator, and assigns the result to the variable. Specifically, the variable
 * is resolved only once, which is important in case the expression that resolves
 * the variable has side-effects.
 * 
 * This expression can only be used with operators that deal with values, not
 * with operators that deal with expressions directly.
 */
public final class BinaryAssignmentExpression extends AbstractComputeExpression {

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
	public BinaryAssignmentExpression(Expression leftHandSide, BinaryOperator operator, Expression rightHandSide) {
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
		final ValueAcceptor leftHandValueAcceptor = leftHandSide.getOrCreateVariable(environment);
		if (leftHandValueAcceptor == null) {
			environment.getRuntime().triggerError("cannot assign to " + leftHandSide);
			return null;
		}
		
		WHAT ABOUT $s[5] .= 'foo' ???
		
		final Object rightHandValue = rightHandSide.evaluate(environment);
		final Object resultValue = operator.applyToValues(leftHandVariable.getValue(), rightHandValue);
		leftHandValueAcceptor.acceptValue(resultValue);
		return resultValue;
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
		dumper.print('=');
		dumper.print(' ');
		rightHandSide.dump(dumper);
		dumper.print(')');
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("binaryAssignment");
		sub.property("operator").string(operator.name());
		leftHandSide.toJson(sub.property("leftHandSide"));
		rightHandSide.toJson(sub.property("rightHandSide"));
		sub.end();
	}

}
