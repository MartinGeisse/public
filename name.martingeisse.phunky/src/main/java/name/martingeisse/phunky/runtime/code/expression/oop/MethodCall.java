/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

import name.martingeisse.phunky.runtime.code.expression.AbstractCallExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;


/**
 * This expression calls a method of an object.
 */
public class MethodCall extends AbstractCallExpression {

	/**
	 * the objectExpression
	 */
	private final Expression objectExpression;
	
	/**
	 * the methodName
	 */
	private final String methodName;

	/**
	 * Constructor.
	 * @param objectExpression the expression that determines the object whose method gets called
	 * @param methodName the name of the method to call
	 * @param parameters the constructor parameters
	 */
	public MethodCall(Expression objectExpression, String methodName, Expression... parameters) {
		super(parameters);
		this.objectExpression = objectExpression;
		this.methodName = methodName;
	}

	/**
	 * Getter method for the objectExpression.
	 * @return the objectExpression
	 */
	public Expression getObjectExpression() {
		return objectExpression;
	}
	
	/**
	 * Getter method for the methodName.
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	
}
