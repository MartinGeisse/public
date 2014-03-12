/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.phunky.runtime.Environment;

/**
 * Base class for expressions that denote a variable.
 */
public abstract class AbstractVariableExpression implements Expression {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public final Object evaluate(Environment environment) {
		return getVariable(environment).getValue();
	}
	
}
