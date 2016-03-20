/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractComputeExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

import org.apache.commons.lang3.NotImplementedException;

/**
 * This expression checks whether a value is an instance of a class.
 */
public class InstanceofExpression extends AbstractComputeExpression {

	/**
	 * the objectExpression
	 */
	private final Expression objectExpression;

	/**
	 * the className
	 */
	private final String className;

	/**
	 * Constructor.
	 * @param objectExpression the expression that determines the object
	 * @param className the name of the class to check for
	 */
	public InstanceofExpression(Expression objectExpression, String className) {
		this.objectExpression = objectExpression;
		this.className = className;
	}

	/**
	 * Getter method for the objectExpression.
	 * @return the objectExpression
	 */
	public Expression getObjectExpression() {
		return objectExpression;
	}

	/**
	 * Getter method for the className.
	 * @return the className
	 */
	public String getClassName() {
		return className;
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
		dumper.print('(');
		objectExpression.dump(dumper);
		dumper.print(" instanceof ");
		dumper.print(className);
		dumper.print(')');
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
