/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code;

/**
 * An expression that refers to a local variable, such as $foo.
 */
public final class LocalVariableExpression implements Expression {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * Constructor.
	 * @param name the name of the variable
	 */
	public LocalVariableExpression(String name) {
		this.name = name;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
}
