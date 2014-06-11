/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;

/**
 * An expression that refers to a defined constant, such as FOO.
 */
public final class ConstantExpression extends AbstractComputeExpression {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * Constructor.
	 * @param name the name of the variable
	 */
	public ConstantExpression(String name) {
		this.name = name;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		// TODO
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print(name);
	}
	
}
