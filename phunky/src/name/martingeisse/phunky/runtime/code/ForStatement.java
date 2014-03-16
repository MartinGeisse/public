/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.value.TypeConversionUtil;

/**
 * The "for" statement.
 */
public final class ForStatement implements Statement {

	/**
	 * the initializationStatement
	 */
	private final Statement initializationStatement;

	/**
	 * the loopCondition
	 */
	private final Expression loopCondition;

	/**
	 * the advanceStatement
	 */
	private final Statement advanceStatement;

	/**
	 * the body
	 */
	private final Statement body;

	/**
	 * Constructor.
	 * @param initializationStatement the initialization statement, executed once at the beginning
	 * @param loopCondition the condition that checks whether to execute the loop once more
	 * @param advanceStatement the advance statement, executed at the end of the body
	 * @param body the body
	 */
	public ForStatement(final Statement initializationStatement, final Expression loopCondition, final Statement advanceStatement, final Statement body) {
		this.initializationStatement = initializationStatement;
		this.loopCondition = loopCondition;
		this.advanceStatement = advanceStatement;
		this.body = body;
	}

	/**
	 * Getter method for the initializationStatement.
	 * @return the initializationStatement
	 */
	public Statement getInitializationStatement() {
		return initializationStatement;
	}

	/**
	 * Getter method for the loopCondition.
	 * @return the loopCondition
	 */
	public Expression getLoopCondition() {
		return loopCondition;
	}

	/**
	 * Getter method for the advanceStatement.
	 * @return the advanceStatement
	 */
	public Statement getAdvanceStatement() {
		return advanceStatement;
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
		initializationStatement.execute(environment);
		while (TypeConversionUtil.convertToBoolean(loopCondition.evaluate(environment))) {
			body.execute(environment);
			advanceStatement.execute(environment);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("for (");
		initializationStatement.dump(dumper);
		dumper.removeRecentNewline();
		dumper.print("; ");
		loopCondition.dump(dumper);
		dumper.print("; ");
		advanceStatement.dump(dumper);
		dumper.removeRecentNewline();
		dumper.println(") {");
		dumper.increaseIndentation();
		body.dump(dumper);
		dumper.decreaseIndentation();
		dumper.println("}");
	}
	
}
