/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * Base class for expressions that denote a variable.
 */
public abstract class AbstractVariableExpression extends AbstractExpression {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		Variable variable = resolveVariable(environment);
		if (variable == null) {
			environment.getRuntime().triggerError("not a variable: " + this, getLocation());
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
		Variable variable = resolveVariable(environment);
		if (variable == null) {
			return null;
		} else {
			return variable.getValue();
		}
	}

	/**
	 * Obtains the variable for this expression, if any.
	 * @param environment the environment
	 * @return the variable or null
	 */
	public abstract Variable resolveVariable(Environment environment);

}
