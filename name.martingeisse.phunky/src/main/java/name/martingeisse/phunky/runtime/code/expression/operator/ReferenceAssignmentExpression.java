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

import org.apache.commons.lang3.NotImplementedException;

/**
 * TODO define exactly what this operator does! It's actually not that easy.
 */
public final class ReferenceAssignmentExpression extends AbstractComputeExpression {

	/**
	 * the leftHandSide
	 */
	private final Expression leftHandSide;

	/**
	 * the rightHandSide
	 */
	private final Expression rightHandSide;

	/**
	 * Constructor.
	 * @param leftHandSide the left-hand side
	 * @param rightHandSide the right-hand side
	 */
	public ReferenceAssignmentExpression(Expression leftHandSide, Expression rightHandSide) {
		this.leftHandSide = leftHandSide;
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
//		final Variable leftHandVariable = leftHandSide.getOrCreateVariable(environment);
//		if (leftHandVariable == null) {
//			environment.getRuntime().triggerError("cannot assign to " + leftHandSide);
//			return null;
//		}
//		final Object rightHandValue = rightHandSide.evaluate(environment);
//		final Object resultValue = operator.applyToValues(leftHandVariable.getValue(), rightHandValue);
//		leftHandVariable.setValue(resultValue);
//		return resultValue;
		throw new NotImplementedException(""); // TODO
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print('(');
		leftHandSide.dump(dumper);
		dumper.print(" =& ");
		rightHandSide.dump(dumper);
		dumper.print(')');
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("referenceAssignment");
		leftHandSide.toJson(sub.property("leftHandSide"));
		rightHandSide.toJson(sub.property("rightHandSide"));
		sub.end();
	}

}
