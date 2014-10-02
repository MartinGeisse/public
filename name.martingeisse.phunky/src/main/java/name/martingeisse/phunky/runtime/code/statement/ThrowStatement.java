/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.Expression;

import org.apache.commons.lang3.NotImplementedException;

/**
 * The "throw" statement.
 */
public final class ThrowStatement extends AbstractStatement {

	/**
	 * the exceptionExpression
	 */
	private final Expression exceptionExpression;

	/**
	 * Constructor.
	 * @param exceptionExpression the expression for the exception
	 */
	public ThrowStatement(final Expression exceptionExpression) {
		this.exceptionExpression = exceptionExpression;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(final Environment environment) {
		throw new NotImplementedException("");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("throw ");
		exceptionExpression.dump(dumper);
		dumper.println(";");
	}
	
}
