/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.assignment.AssignmentTarget;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * Base class for computed (non-variable) expressions.
 */
public abstract class AbstractComputeExpression extends AbstractExpression {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluateForEmptyCheck(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluateForEmptyCheck(Environment environment) {
		return evaluate(environment);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public final Variable resolveOrCreateVariable(Environment environment) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#resolveValueAcceptor(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public AssignmentTarget resolveValueAcceptor(Environment environment) {
		environment.getRuntime().triggerError("cannot assign to " + this);
		return null;
	}
	
}
