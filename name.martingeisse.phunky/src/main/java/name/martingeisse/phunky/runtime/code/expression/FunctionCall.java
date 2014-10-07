/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Callable;
import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;

/**
 * This expression calls a globally defined function.
 */
public final class FunctionCall extends AbstractCallExpression {

	/**
	 * the nameExpression
	 */
	private final Expression nameExpression;

	/**
	 * Convenience constructor.
	 * @param name the function name
	 * @param parameters the parameters
	 */
	public FunctionCall(final String name, final Expression... parameters) {
		this(new LiteralExpression(name), parameters);
	}

	/**
	 * Constructor.
	 * @param nameExpression the expression that determines the function name
	 * @param parameters the parameters
	 */
	public FunctionCall(final Expression nameExpression, final Expression... parameters) {
		super(parameters);
		this.nameExpression = nameExpression;
	}
	
	/**
	 * Getter method for the nameExpression.
	 * @return the nameExpression
	 */
	public Expression getNameExpression() {
		return nameExpression;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(final Environment environment) {
		Object name = nameExpression.evaluate(environment);
		if (!(name instanceof String)) {
			environment.getRuntime().triggerError("function name must be a string: " + name);
			return null;
		}
		Callable function = environment.getRuntime().getFunctions().get(name);
		if (function == null) {
			environment.getRuntime().triggerError("undefined function: " + name);
			return null;
		} else {
			return function.call(environment.getRuntime(), evaluateParameters(environment));
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		nameExpression.dump(dumper);
		dumpParameters(dumper);
	}
	
}
