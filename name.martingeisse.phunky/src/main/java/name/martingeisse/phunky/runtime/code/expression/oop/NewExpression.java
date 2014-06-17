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
