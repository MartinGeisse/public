/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * The "while" statement.
 */
public final class WhileStatement extends AbstractStatement {

	/**
	 * the loopCondition
	 */
	private final Expression loopCondition;

	/**
	 * the body
	 */
	private final Statement body;

	/**
	 * Constructor.
	 * @param loopCondition the condition that checks whether to execute the loop once more
	 * @param body the body
	 */
	public WhileStatement(final Expression loopCondition, final Statement body) {
		this.loopCondition = loopCondition;
		this.body = body;
	}

	/**
	 * Getter method for the loopCondition.
	 * @return the loopCondition
	 */
	public Expression getLoopCondition() {
		return loopCondition;
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
		environment.getRuntime().getLog().beginStatement("while");
		try {
			while (TypeConversionUtil.convertToBoolean(loopCondition.evaluate(environment))) {
				body.execute(environment);
			}
		} catch (BreakException e) {
			environment.getRuntime().getLog().endStatement("while", "break");
			if (e.onBreakLoop()) {
				throw e;
			} else {
				return;
			}
		}
		environment.getRuntime().getLog().endStatement("while");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("while (");
		loopCondition.dump(dumper);
		dumper.println(") {");
		dumper.increaseIndentation();
		body.dump(dumper);
		dumper.decreaseIndentation();
		dumper.println("}");
	}

}
