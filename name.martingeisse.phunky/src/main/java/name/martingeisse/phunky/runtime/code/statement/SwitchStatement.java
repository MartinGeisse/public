/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import org.apache.commons.lang3.NotImplementedException;
import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.Expression;

/**
 * The "switch (...) {...}" statement.
 */
public final class SwitchStatement implements Statement {

	/**
	 * the expression
	 */
	private final Expression expression;

	/**
	 * the properCases
	 */
	private final SwitchCase[] properCases;

	/**
	 * the defaultCaseStatement
	 */
	private final Statement defaultCaseStatement;

	/**
	 * Constructor.
	 * @param expression the switch expression
	 * @param properCases the "proper" (non-default) cases
	 * @param defaultCaseStatement the statement for the default case
	 */
	public SwitchStatement(final Expression expression, final SwitchCase[] properCases, final Statement defaultCaseStatement) {
		this.expression = expression;
		this.properCases = properCases;
		this.defaultCaseStatement = defaultCaseStatement;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(final Environment environment) {
		throw new NotImplementedException("");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(final CodeDumper dumper) {
		dumper.print("switch (");
		expression.dump(dumper);
		dumper.println(") {");
		dumper.increaseIndentation();
		for (SwitchCase switchCase : properCases) {
			dumper.println("");
			switchCase.dump(dumper);
		}
		dumper.println("");
		dumper.println("default:");
		dumper.increaseIndentation();
		defaultCaseStatement.dump(dumper);
		dumper.decreaseIndentation();
		dumper.println("");
		dumper.decreaseIndentation();
		dumper.println("}");
	}

}
