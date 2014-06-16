/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

import org.apache.commons.lang3.NotImplementedException;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractCallExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;


/**
 * This expression calls a method of an object.
 */
public class StaticMethodCall extends AbstractCallExpression {

	/**
	 * the className
	 */
	private final String className;
	
	/**
	 * the methodName
	 */
	private final String methodName;

	/**
	 * Constructor.
	 * @param className the expression that determines the object whose method gets called
	 * @param methodName the name of the method to call
	 * @param parameters the constructor parameters
	 */
	public StaticMethodCall(String className, String methodName, Expression... parameters) {
		super(parameters);
		this.className = Self.normalize(className);
		this.methodName = methodName;
	}

	/**
	 * Getter method for the className.
	 * @return the className
	 */
	public String getClassName() {
		return className;
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
