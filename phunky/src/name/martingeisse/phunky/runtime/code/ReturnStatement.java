/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.phunky.runtime.Environment;

/**
 * The "return" statement.
 */
public final class ReturnStatement implements Statement {

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
		Object returnValue = (returnValueExpression == null ? null : returnValueExpression.evaluate(environment));
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
	
}
