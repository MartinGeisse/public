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
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * The "for" statement.
 */
public final class ForStatement extends AbstractStatement {

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
		environment.getRuntime().getLog().beginStatement("for");
		try {
			initializationStatement.execute(environment);
			while (TypeConversionUtil.convertToBoolean(loopCondition.evaluate(environment))) {
				body.execute(environment);
				advanceStatement.execute(environment);
			}
		} catch (BreakException e) {
			environment.getRuntime().getLog().endStatement("for", "break");
			if (e.onBreakLoop()) {
				throw e;
			} else {
				return;
			}
		}
		environment.getRuntime().getLog().endStatement("for");
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

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("for");
		initializationStatement.toJson(sub.property("initializationStatement"));
		loopCondition.toJson(sub.property("loopCondition"));
		advanceStatement.toJson(sub.property("advanceStatement"));
		body.toJson(sub.property("body"));
		sub.end();
	}

}
