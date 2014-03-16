/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.phunky.runtime.Callable;
import name.martingeisse.phunky.runtime.Environment;

/**
 * This expression calls a globally defined function.
 */
public final class FunctionCall extends AbstractCallExpression {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * Constructor.
	 * @param name the function name
	 * @param parameters the parameters
	 */
	public FunctionCall(final String name, final Expression... parameters) {
		super(parameters);
		this.name = name;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(final Environment environment) {
		Callable function = environment.getRuntime().getFunctions().get(name);
		if (function == null) {
			environment.getRuntime().triggerError("undefined function: " + name);
			return null;
		} else {
			return function.call(evaluateParameters(environment));
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print(name);
		dumpParameters(dumper);
	}
	
}
