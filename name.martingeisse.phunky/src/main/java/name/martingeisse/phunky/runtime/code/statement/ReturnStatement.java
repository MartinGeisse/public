/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

/**
 * The "return" statement.
 */
public final class ReturnStatement extends AbstractStatement {

	/**
	 * the returnValueExpression
	 */
	private final Expression returnValueExpression;

	/**
	 * Constructor.
	 * @param returnValueExpression the expression for the return value
	 */
	public ReturnStatement(final Expression returnValueExpression) {
		this.returnValueExpression = returnValueExpression;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(final Environment environment) {
		environment.getRuntime().getLog().beginStatement("return");
		Object returnValue = (returnValueExpression == null ? null : returnValueExpression.evaluate(environment));
		environment.getRuntime().getLog().endStatement("return");
		throw new ReturnException(returnValue);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		if (returnValueExpression == null) {
			dumper.println("return;");
		} else {
			dumper.print("return ");
			returnValueExpression.dump(dumper);
			dumper.println(";");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("return");
		if (returnValueExpression == null) {
			sub.property("expression").nullLiteral().end();
		} else {
			returnValueExpression.toJson(sub.property("expression"));
			sub.end();
		}
	}

}
