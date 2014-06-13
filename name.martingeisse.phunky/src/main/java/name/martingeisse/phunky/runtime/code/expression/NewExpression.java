/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression;

/**
 * Represents a "new SomeClass(...)" expression.
 */
public class NewExpression extends AbstractCallExpression {

	/**
	 * the className
	 */
	private final String className;

	/**
	 * Constructor.
	 * @param className the name of the class to instantiate
	 * @param parameters the constructor parameters
	 */
	public NewExpression(String className, Expression... parameters) {
		super(parameters);
		this.className = className;
	}

	/**
	 * Getter method for the className.
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	
}
