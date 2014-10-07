/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.code.expression.operator.BinaryOperator;
import name.martingeisse.phunky.runtime.json.JsonListBuilder;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

/**
 * The "switch (...) {...}" statement.
 */
public final class SwitchStatement extends AbstractStatement {

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
		environment.getRuntime().getLog().beginStatement("switch");
		Object switchValue = expression.evaluate(environment);
		environment.getRuntime().getLog().log("switch value: " + switchValue);
		try {
			matching: {
				for (SwitchCase switchCase : properCases) {
					Object caseValue = switchCase.getExpression().evaluate(environment);
					boolean match = (Boolean)BinaryOperator.EQUALS.applyToValues(switchValue, caseValue);
					if (match) {
						environment.getRuntime().getLog().log("matching case: " + caseValue);
						switchCase.getStatement().execute(environment);
						break matching;
					}
				}
				environment.getRuntime().getLog().log("no proper case matched; checking for default case");
				if (defaultCaseStatement != null) {
					defaultCaseStatement.execute(environment);
				}
			}
		} catch (BreakException e) {
			environment.getRuntime().getLog().endStatement("switch", "break");
			if (e.onBreakLoop()) {
				throw e;
			} else {
				return;
			}
		}
		environment.getRuntime().getLog().endStatement("switch");
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

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("switch");
		expression.toJson(sub.property("expression"));
		JsonListBuilder<?> subsub = sub.property("cases").list();
		for (SwitchCase switchCase : properCases) {
			switchCase.toJson(subsub.element());
		}
		subsub.end();
		defaultCaseStatement.toJson(sub.property("default"));
		sub.end();
	}

}
