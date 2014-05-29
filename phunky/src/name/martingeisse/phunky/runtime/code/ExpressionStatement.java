/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.phunky.runtime.Environment;

/**
 * This statement evaluates an expression and discards the result.
 */
public final class ExpressionStatement implements Statement {

	/**
	 * the expression
	 */
	private final Expression expression;

	/**
	 * Constructor.
	 * @param expression the expression
	 */
	public ExpressionStatement(final Expression expression) {
		this.expression = expression;
	}

	/**
	 * Getter method for the expression.
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(Environment environment) {
		expression.evaluate(environment);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		expression.dump(dumper);
		dumper.println(";");
	}
	
}
