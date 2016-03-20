/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractCallExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

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
	 * the methodNameExpression
	 */
	private final Expression methodNameExpression;

	/**
	 * Constructor.
	 * @param objectExpression the expression that determines the object whose method gets called
	 * @param methodNameExpression the expression that determines the name of the method to call
	 * @param parameters the constructor parameters
	 */
	public InstanceMethodCall(Expression objectExpression, Expression methodNameExpression, Expression... parameters) {
		super(parameters);
		this.objectExpression = objectExpression;
		this.methodNameExpression = methodNameExpression;
	}

	/**
	 * Getter method for the objectExpression.
	 * @return the objectExpression
	 */
	public Expression getObjectExpression() {
		return objectExpression;
	}

	/**
	 * Getter method for the methodNameExpression.
	 * @return the methodNameExpression
	 */
	public Expression getMethodNameExpression() {
		return methodNameExpression;
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
		objectExpression.dump(dumper);
		dumper.print("->");
		methodNameExpression.dump(dumper);
		dumpArgumentExpressions(dumper);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		// TODO
		throw new NotImplementedException("");
	}

}
