/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.phunky.runtime.Callable;
import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.Variable;

/**
 * This statement defines a (global) function.
 */
public final class FunctionDefinition implements Statement {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * the parameterNames
	 */
	private final String[] parameterNames;

	/**
	 * the body
	 */
	private final Statement body;

	/**
	 * Constructor.
	 * @param name the function name
	 * @param parameterNames the parameter names
	 * @param body the function body
	 */
	public FunctionDefinition(final String name, final String[] parameterNames, final Statement body) {
		this.name = name;
		this.parameterNames = parameterNames.clone();
		this.body = body;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the number of parameter names.
	 * @return the number of parameter names
	 */
	public int getParameterCount() {
		return parameterNames.length;
	}

	/**
	 * Getter method for a single parameter name.
	 * @param index the index
	 * @return the parameter
	 */
	public String getParameter(final int index) {
		return parameterNames[index];
	}

	/**
	 * Getter method for the body.
	 * @return the body
	 */
	public Statement getBody() {
		return body;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(final Environment environment) {
		environment.getRuntime().getFunctions().put(name, new Callable() {
			@Override
			public Object call(final Object[] arguments) {
				final Environment childEnvironment = new Environment(environment.getRuntime());
				final int bindCount;
				if (arguments.length < parameterNames.length) {
					childEnvironment.getRuntime().triggerError("too few parameters for function " + FunctionDefinition.this);
					bindCount = arguments.length;
				} else {
					bindCount = parameterNames.length;
				}
				for (int i=0; i<bindCount; i++) {
					childEnvironment.put(parameterNames[i], new Variable(arguments[i]));
				}
				try {
					body.execute(childEnvironment);
					return null;
				} catch (ReturnException e) {
					return e.getReturnValue();
				}
			}
		});
	}

}
