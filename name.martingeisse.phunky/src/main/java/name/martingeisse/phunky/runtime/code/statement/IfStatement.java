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
 * An if or if/else statement.
 */
public final class IfStatement extends AbstractStatement {

	/**
	 * the condition
	 */
	private final Expression condition;

	/**
	 * the thenBranch
	 */
	private final Statement thenBranch;

	/**
	 * the elseBranch
	 */
	private final Statement elseBranch;

	/**
	 * Constructor.
	 * @param condition the condition expression
	 * @param thenBranch the "then" branch
	 * @param elseBranch the "else" branch
	 */
	public IfStatement(final Expression condition, final Statement thenBranch, final Statement elseBranch) {
		this.condition = condition;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	/**
	 * Getter method for the condition.
	 * @return the condition
	 */
	public Expression getCondition() {
		return condition;
	}

	/**
	 * Getter method for the thenBranch.
	 * @return the thenBranch
	 */
	public Statement getThenBranch() {
		return thenBranch;
	}

	/**
	 * Getter method for the elseBranch.
	 * @return the elseBranch
	 */
	public Statement getElseBranch() {
		return elseBranch;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(final Environment environment) {
		environment.getRuntime().getLog().beginStatement("if");
		boolean conditionResult = TypeConversionUtil.convertToBoolean(condition.evaluate(environment));
		if (conditionResult) {
			if (thenBranch != null) {
				thenBranch.execute(environment);
			}
		} else {
			if (elseBranch != null) {
				elseBranch.execute(environment);
			}
		}
		environment.getRuntime().getLog().endStatement("if");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("if (");
		condition.dump(dumper);
		dumper.println(") {");
		if (thenBranch != null) {
			dumper.increaseIndentation();
			thenBranch.dump(dumper);
			dumper.decreaseIndentation();
		}
		if (elseBranch != null) {
			dumper.println("} else {");
			dumper.increaseIndentation();
			elseBranch.dump(dumper);
			dumper.decreaseIndentation();
		}
		dumper.println("}");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("if");
		condition.toJson(sub.property("condition"));
		thenBranch.toJson(sub.property("thenBranch"));
		elseBranch.toJson(sub.property("elseBranch"));
		sub.end();
	}

}
