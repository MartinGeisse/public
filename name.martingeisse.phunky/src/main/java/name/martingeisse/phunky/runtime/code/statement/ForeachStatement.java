/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.value.PhpIterable;

/**
 * The "foreach" statement.
 */
public final class ForeachStatement implements Statement {

	/**
	 * the containerExpression
	 */
	private final Expression containerExpression;

	/**
	 * the keyIterationVariableName
	 */
	private final String keyIterationVariableName;

	/**
	 * the valueIterationVariableName
	 */
	private final String valueIterationVariableName;
	
	/**
	 * the body
	 */
	private final Statement body;

	/**
	 * Constructor.
	 * @param containerExpression the expression that evaluates to the container being iterated over
	 * @param keyIterationVariableName the name of the key iteration variable
	 * @param valueIterationVariableName the name of the value iteration variable
	 * @param body the body
	 */
	public ForeachStatement(final Expression containerExpression, final String keyIterationVariableName, final String valueIterationVariableName, final Statement body) {
		this.containerExpression = containerExpression;
		this.keyIterationVariableName = keyIterationVariableName;
		this.valueIterationVariableName = valueIterationVariableName;
		this.body = body;
	}

	/**
	 * Getter method for the containerExpression.
	 * @return the containerExpression
	 */
	public Expression getContainerExpression() {
		return containerExpression;
	}

	/**
	 * Getter method for the keyIterationVariableName.
	 * @return the keyIterationVariableName
	 */
	public String getKeyIterationVariableName() {
		return keyIterationVariableName;
	}
	
	/**
	 * Getter method for the valueIterationVariableName.
	 * @return the valueIterationVariableName
	 */
	public String getValueIterationVariableName() {
		return valueIterationVariableName;
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
		try {
			Object container = containerExpression.evaluate(environment);
			if (!(container instanceof PhpIterable)) {
				environment.getRuntime().triggerError("invalid argument for foreach: " + container);
			} else {
				PhpIterable iterable = (PhpIterable)container;
				iterable.iterate(environment, keyIterationVariableName, valueIterationVariableName, body);
			}
		} catch (BreakException e) {
			if (e.onBreakLoop()) {
				throw e;
			}
		}

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("foreach (");
		containerExpression.dump(dumper);
		dumper.print(" as ");
		if (keyIterationVariableName != null) {
			dumper.print(keyIterationVariableName);
			dumper.print(" => ");
		}
		dumper.print(valueIterationVariableName);
		dumper.println(") {");
		dumper.increaseIndentation();
		body.dump(dumper);
		dumper.decreaseIndentation();
		dumper.println("}");
	}

}
