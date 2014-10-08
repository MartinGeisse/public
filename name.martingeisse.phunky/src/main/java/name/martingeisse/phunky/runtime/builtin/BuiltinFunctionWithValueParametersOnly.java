/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.code.expression.Expression;

/**
 * Helper class to simplify the implementation of built-in functions
 * that only take value parameters.
 */
public abstract class BuiltinFunctionWithValueParametersOnly extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.PhpCallable#call(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.expression.Expression[])
	 */
	@Override
	public final Object call(Environment environment, Expression[] argumentExpressions) {
		final Object[] arguments = new Object[argumentExpressions.length];
		for (int i=0; i<arguments.length; i++) {
			arguments[i] = argumentExpressions[i].evaluate(environment);
		}
		return call(environment.getRuntime(), arguments);
	}

	/**
	 * The actual implementation of this function.
	 * @param runtime the PHP runtime
	 * @param arguments the argument values
	 * @return the return value
	 */
	public abstract Object call(PhpRuntime runtime, Object[] arguments);

}
