/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractCallExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;

import org.apache.commons.lang3.NotImplementedException;


/**
 * This expression calls a method of an object.
 */
public class InstanceMethodCall extends AbstractCallExpression {

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
	public InstanceMethodCall(Expression objectExpression, String methodName, Expression... parameters) {
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

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		// TODO
		throw new NotImplementedException("");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		// TODO
		throw new NotImplementedException("");
	}
	
}
