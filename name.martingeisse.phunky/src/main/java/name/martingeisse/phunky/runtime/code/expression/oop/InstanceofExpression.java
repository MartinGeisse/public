/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractComputeExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
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
		// TODO
		throw new NotImplementedException("");
	}
	
}
