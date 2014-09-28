/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.array;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
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
		environment.getRuntime().triggerError("cannot evaluate an append-to-array expression");
		return null;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluateForEmptyCheck(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluateForEmptyCheck(Environment environment) {
		environment.getRuntime().triggerError("cannot evaluate-for-empty-check an append-to-array expression");
		return null;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getVariable(Environment environment) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getOrCreateVariable(Environment environment) {
		// note that arrays are a value type, so getting the variable for an element also gets the variable for the array
		Variable arrayVariable = arrayExpression.getOrCreateVariable(environment);
		if (arrayVariable == null) {
			environment.getRuntime().triggerError("cannot use this expression as an array: " + arrayExpression);
			return null;
		}
		PhpVariableArray variableArray = arrayVariable.getVariableArray(environment.getRuntime());
		if (variableArray == null) {
			return null;
		} else {
			return variableArray.append();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		arrayExpression.dump(dumper);
		dumper.print("[]");
	}

}
