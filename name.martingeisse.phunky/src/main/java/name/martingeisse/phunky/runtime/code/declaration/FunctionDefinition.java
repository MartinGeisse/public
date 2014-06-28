/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.declaration;

import name.martingeisse.phunky.runtime.Callable;
import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.Variable;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.statement.ReturnException;
import name.martingeisse.phunky.runtime.code.statement.Statement;

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
	 * the parameterDefaultValues
	 */
	private final Object[] parameterDefaultValues;

	/**
	 * the body
	 */
	private final Statement body;

	/**
	 * Constructor.
	 * @param name the function name
	 * @param parameterNames the parameter names
	 * @param parameterDefaultValues the parameter default values
	 * @param body the function body
	 */
	public FunctionDefinition(final String name, final String[] parameterNames, final Object[] parameterDefaultValues, final Statement body) {
		this.name = name;
		this.parameterNames = parameterNames.clone();
		this.parameterDefaultValues = parameterDefaultValues.clone();
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
	 * Getter method for the number of parameter default values.
	 * @return the number of parameter default values
	 */
	public int getParameterDefaultValueCount() {
		return parameterDefaultValues.length;
	}
	
	/**
	 * Getter method for a single parameter default value.
	 * @param index the index
	 * @return the parameter default value
	 */
	public Object getParameterDefaultValue(final int index) {
		return parameterDefaultValues[index];
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
			public Object call(PhpRuntime runtime, Object[] arguments) {
				final Environment childEnvironment = new Environment(runtime);
				final int parameterBindCount;
				if (arguments.length + parameterDefaultValues.length < parameterNames.length) {
					runtime.triggerError("too few parameters for function " + FunctionDefinition.this);
					parameterBindCount = arguments.length;
				} else {
					parameterBindCount = parameterNames.length;
				}
				int defaultValuesStartIndex = (parameterNames.length - parameterDefaultValues.length);
				for (int i=0; i<parameterDefaultValues.length; i++) {
					childEnvironment.put(parameterNames[defaultValuesStartIndex + i], new Variable(parameterDefaultValues[i]));
				}
				for (int i=0; i<parameterBindCount; i++) {
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

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("function ");
		dumper.print(name);
		dumper.print('(');
		boolean first = true;
		for (String parameterName : parameterNames) {
			if (first) {
				first = false;
			} else {
				dumper.print(", ");
			}
			dumper.print(parameterName);
		}
		dumper.println(") {");
		dumper.increaseIndentation();
		body.dump(dumper);
		dumper.decreaseIndentation();
		dumper.println("}");
	}
	
}
