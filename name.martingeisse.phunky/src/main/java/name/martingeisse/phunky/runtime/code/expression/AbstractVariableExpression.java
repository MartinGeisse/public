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
	public final Object evaluate(Environment environment) {
		Variable variable = getVariable(environment);
		if (variable == null) {
			environment.getRuntime().triggerError("undefined variable: " + this);
			return null;
		} else {
			return variable.getValue();
		}
	}
	
}
