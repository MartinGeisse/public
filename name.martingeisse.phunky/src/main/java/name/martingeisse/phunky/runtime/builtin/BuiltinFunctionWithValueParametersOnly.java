/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.code.expression.Expression;

/**
 * Helper class to simplify the implementation of built-in functions
 * that only take value parameters.
 */
public abstract class BuiltinFunctionWithValueParametersOnly extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.PhpCallable#call(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.CodeLocation, name.martingeisse.phunky.runtime.code.expression.Expression[])
	 */
	@Override
	public Object call(Environment environment, CodeLocation location, Expression[] argumentExpressions) {
		final Object[] arguments = new Object[argumentExpressions.length];
		for (int i=0; i<arguments.length; i++) {
			arguments[i] = argumentExpressions[i].evaluate(environment);
		}
		return call(environment.getRuntime(), location, arguments);
	}

	/**
	 * The actual implementation of this function.
	 * @param runtime the PHP runtime
	 * @param location the location in code, used for error reporting
	 * @param arguments the argument values
	 * @return the return value
	 */
	public abstract Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments);

}
