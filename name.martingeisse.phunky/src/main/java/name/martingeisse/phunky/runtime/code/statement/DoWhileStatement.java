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
 * The "do..while" statement.
 */
public final class DoWhileStatement extends AbstractStatement {

	/**
	 * the body
	 */
	private final Statement body;
	
	/**
	 * the loopCondition
	 */
	private final Expression loopCondition;

	/**
	 * Constructor.
	 * @param body the body
	 * @param loopCondition the condition that checks whether to execute the loop once more
	 */
	public DoWhileStatement(final Statement body, final Expression loopCondition) {
		this.body = body;
		this.loopCondition = loopCondition;
	}

	/**
	 * Getter method for the body.
	 * @return the body
	 */
	public Statement getBody() {
		return body;
	}
	
	/**
	 * Getter method for the loopCondition.
	 * @return the loopCondition
	 */
	public Expression getLoopCondition() {
		return loopCondition;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(final Environment environment) {
		environment.getRuntime().getLog().beginStatement("do..while");
		try {
			do {
				body.execute(environment);
			} while (TypeConversionUtil.convertToBoolean(loopCondition.evaluate(environment)));
		} catch (BreakException e) {
			environment.getRuntime().getLog().endStatement("do..while", "break");
			if (e.onBreakLoop()) {
				throw e;
			} else {
				return;
			}
		}
		environment.getRuntime().getLog().endStatement("do..while");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.println("do {");
		dumper.increaseIndentation();
		body.dump(dumper);
		dumper.decreaseIndentation();
		dumper.println("} while (");
		loopCondition.dump(dumper);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("doWhile");
		body.toJson(sub.property("body"));
		loopCondition.toJson(sub.property("loopCondition"));
		sub.end();
	}

}
