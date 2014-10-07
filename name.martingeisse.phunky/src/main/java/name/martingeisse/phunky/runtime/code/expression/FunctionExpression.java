/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.statement.Statement;
import name.martingeisse.phunky.runtime.json.JsonListBuilder;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

import org.apache.commons.lang3.NotImplementedException;

/**
 * This statement defines a local function expression.
 */
public final class FunctionExpression extends AbstractComputeExpression {

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
	 * @param parameterNames the parameter names
	 * @param body the function body
	 */
	public FunctionExpression(final String[] parameterNames, final Statement body) {
		this.parameterNames = parameterNames.clone();
		this.body = body;
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
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		/*
		environment.getRuntime().getFunctions().put(name, new Callable() {
			@Override
			public Object call(PhpRuntime runtime, Object[] arguments) {
				final Environment childEnvironment = new Environment(runtime);
				final int bindCount;
				if (arguments.length < parameterNames.length) {
					runtime.triggerError("too few parameters for function " + FunctionExpression.this);
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
		*/
		throw new NotImplementedException("");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("function(");
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

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("function");
		JsonListBuilder<?> subsub = sub.property("parameters").list();
		for (String parameterName : parameterNames) {
			subsub.element().string(parameterName);
		}
		subsub.end();
		body.toJson(sub.property("body"));
		sub.end();
	}

}
