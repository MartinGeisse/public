/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.Variable;

/**
 * Base class for expressions that denote a variable.
 */
public abstract class AbstractVariableExpression implements Expression {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		Variable variable = getVariable(environment);
		if (variable == null) {
			environment.getRuntime().triggerError("not a variable: " + this);
			return null;
		} else {
			return variable.getValue();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluateForEmptyCheck(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluateForEmptyCheck(Environment environment) {
		Variable variable = getVariable(environment);
		if (variable == null) {
			return null;
		} else {
			return variable.getValue();
		}
	}

}
