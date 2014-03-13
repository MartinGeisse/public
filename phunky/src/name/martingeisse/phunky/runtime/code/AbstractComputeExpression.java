/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.Variable;

/**
 * Base class for computed (non-variable) expressions.
 */
public abstract class AbstractComputeExpression implements Expression {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#getVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public final Variable getVariable(Environment environment) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getOrCreateVariable(Environment environment) {
		return null;
	}
	
}
