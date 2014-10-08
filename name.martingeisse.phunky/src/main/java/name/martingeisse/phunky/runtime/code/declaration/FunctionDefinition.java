/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.declaration;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.PhpCallable;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.code.expression.LiteralExpression;
import name.martingeisse.phunky.runtime.code.statement.AbstractStatement;
import name.martingeisse.phunky.runtime.code.statement.ReturnException;
import name.martingeisse.phunky.runtime.code.statement.Statement;
import name.martingeisse.phunky.runtime.json.JsonListBuilder;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

/**
 * This statement defines a (global) function.
 */
public final class FunctionDefinition extends AbstractStatement {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * the parameters
	 */
	private final ParameterDeclaration[] parameters;

	/**
	 * the body
	 */
	private final Statement body;

	/**
	 * Constructor.
	 * @param name the function name
	 * @param parameters the parameters
	 * @param body the function body
	 */
	public FunctionDefinition(final String name, final ParameterDeclaration[] parameters, final Statement body) {
		this.name = name;
		this.parameters = parameters;
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
		return parameters.length;
	}

	/**
	 * Getter method for a single parameter name.
	 * @param index the index
	 * @return the parameter
	 */
	public ParameterDeclaration getParameter(final int index) {
		return parameters[index];
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
	public void execute(final Environment definitionEnvironment) {
		definitionEnvironment.getRuntime().getLog().beginStatement("function");
		definitionEnvironment.getRuntime().getFunctions().put(name, new PhpCallable() {
			@Override
			public Object call(Environment callerEnvironment, Expression[] argumentExpressions) {
				final String contextDescription = "function " + name + "()";
				final Environment calleeEnvironment = new Environment(callerEnvironment.getRuntime());
				for (int i=0; i<parameters.length; i++) {
					Expression argumentExpression = (i < argumentExpressions.length ? argumentExpressions[i] : null);
					parameters[i].bind(callerEnvironment, calleeEnvironment, argumentExpression, contextDescription);
				}
				try {
					body.execute(calleeEnvironment);
					return null;
				} catch (ReturnException e) {
					return e.getReturnValue();
				}
			}
		});
		definitionEnvironment.getRuntime().getLog().endStatement("function");
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
		for (ParameterDeclaration parameter : parameters) {
			if (first) {
				first = false;
			} else {
				dumper.print(", ");
			}
			if (parameter.isReferenceParameter()) {
				dumper.print('&');
			}
			dumper.print(parameter.getName());
			if (parameter.isHasDefaultValue()) {
				dumper.print(" = ");
				LiteralExpression.dumpLiteral(parameter.getDefaultValue(), dumper);
			}
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
		JsonObjectBuilder<?> sub = builder.object().property("type").string("functionDefinition");
		sub.property("name").string(name);
		JsonListBuilder<?> subsub = sub.property("parameters").list();
		for (ParameterDeclaration parameter : parameters) {
			JsonObjectBuilder<?> subsubsub = subsub.element().object();
			parameterToJson(subsubsub, parameter);
			subsubsub.end();
		}
		subsub.end();
		body.toJson(sub.property("body"));
		sub.end();
	}
	
	/**
	 * 
	 */
	private void parameterToJson(JsonObjectBuilder<?> builder, ParameterDeclaration parameter) {
		builder.property("name").string(parameter.getName());
		builder.property("reference").bool(parameter.isReferenceParameter());
		if (parameter.isHasDefaultValue()) {
			if (!LiteralExpression.literalToJsonValue(parameter.getDefaultValue(), builder.property("default"))) {
				builder.property("defaultValueStatus").string("UNKNOWN_TYPE");
			}
		}
	}

}
