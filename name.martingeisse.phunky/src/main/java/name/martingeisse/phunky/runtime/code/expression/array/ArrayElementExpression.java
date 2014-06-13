/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.array;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.Variable;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractVariableExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;

/**
 * This expression selects one element of an array.
 */
public final class ArrayElementExpression extends AbstractVariableExpression {

	/**
	 * the arrayExpression
	 */
	private final Expression arrayExpression;

	/**
	 * the keyExpression
	 */
	private final Expression keyExpression;

	/**
	 * Constructor.
	 * @param arrayExpression the expression that determines the array
	 * @param keyExpression the expression that determines the key
	 */
	public ArrayElementExpression(final Expression arrayExpression, final Expression keyExpression) {
		this.arrayExpression = arrayExpression;
		this.keyExpression = keyExpression;
	}

	/**
	 * Getter method for the arrayExpression.
	 * @return the arrayExpression
	 */
	public Expression getArrayExpression() {
		return arrayExpression;
	}

	/**
	 * Getter method for the keyExpression.
	 * @return the keyExpression
	 */
	public Expression getKeyExpression() {
		return keyExpression;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getVariable(final Environment environment) {
		// TODO
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getOrCreateVariable(final Environment environment) {
		// TODO
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(final CodeDumper dumper) {
		arrayExpression.dump(dumper);
		dumper.print('[');
		keyExpression.dump(dumper);
		dumper.print(']');
	}

}
