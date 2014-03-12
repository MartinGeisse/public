/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.phunky.runtime.Environment;

/**
 * An expression that just returns a fixed value.
 */
public final class LiteralExpression extends AbstractComputeExpression {

	/**
	 * the value
	 */
	private final Object value;

	/**
	 * Constructor.
	 * @param value the value of this literal
	 */
	public LiteralExpression(Object value) {
		this.value = value;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		return value;
	}
	
}
