/**
 * Copyright (c) 2013 Shopgate GmbH
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
public class StaticMethodCall extends AbstractCallExpression {

	/**
	 * the classNameExpression
	 */
	private final Expression classNameExpression;
	
	/**
	 * the methodNameExpression
	 */
	private final Expression methodNameExpression;

	/**
	 * Constructor.
	 * @param classNameExpression the expression that determines the object whose method gets called
	 * @param methodNameExpression the name of the method to call
	 * @param parameters the constructor parameters
	 */
	public StaticMethodCall(Expression classNameExpression, Expression methodNameExpression, Expression... parameters) {
		super(parameters);
		this.classNameExpression = classNameExpression;
		this.methodNameExpression = methodNameExpression;
	}

	/**
	 * Getter method for the classNameExpression.
	 * @return the classNameExpression
	 */
	public Expression getClassNameExpression() {
		return classNameExpression;
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
		classNameExpression.dump(dumper);
		dumper.print("::");
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
