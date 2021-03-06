/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.array;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.assignment.ArrayAppendAssignmentTarget;
import name.martingeisse.phunky.runtime.assignment.AssignmentTarget;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;
import name.martingeisse.phunky.runtime.variable.PhpVariableArray;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * This expression can be used to append an element to an array.
 */
public final class ArrayAppendExpression extends AbstractExpression {

	/**
	 * the arrayExpression
	 */
	private final Expression arrayExpression;

	/**
	 * Constructor.
	 * @param arrayExpression the expression that determines the array
	 */
	public ArrayAppendExpression(final Expression arrayExpression) {
		this.arrayExpression = arrayExpression;
	}

	/**
	 * Getter method for the arrayExpression.
	 * @return the arrayExpression
	 */
	public Expression getArrayExpression() {
		return arrayExpression;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		environment.getRuntime().triggerError("cannot evaluate an append-to-array expression", getLocation());
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluateForEmptyCheck(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluateForEmptyCheck(Environment environment) {
		environment.getRuntime().triggerError("cannot evaluate-for-empty-check an append-to-array expression", getLocation());
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable resolveOrCreateVariable(Environment environment) {
		PhpVariableArray variableArray = obtainArray(environment);
		return (variableArray == null ? null : variableArray.append());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#resolveAssignmentTarget(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public AssignmentTarget resolveAssignmentTarget(Environment environment) {
		return new ArrayAppendAssignmentTarget(obtainArray(environment));
	}

	/**
	 * 
	 */
	private PhpVariableArray obtainArray(Environment environment) {
		// note that arrays are a value type, so getting the variable for an element also gets the variable for the array
		Variable arrayVariable = arrayExpression.resolveOrCreateVariable(environment);
		if (arrayVariable == null) {
			environment.getRuntime().triggerError("cannot use this expression as an array: " + arrayExpression, arrayExpression.getLocation());
			return null;
		}
		return arrayVariable.getVariableArray(environment.getRuntime(), getLocation());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		arrayExpression.dump(dumper);
		dumper.print("[]");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("arrayAppend");
		arrayExpression.toJson(sub.property("array"));
		sub.end();
	}

}
