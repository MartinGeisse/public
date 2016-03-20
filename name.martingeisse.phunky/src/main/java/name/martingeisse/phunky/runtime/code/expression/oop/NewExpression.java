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
 * Represents a "new SomeClass(...)" expression.
 */
public class NewExpression extends AbstractCallExpression {

	/**
	 * the classNameExpression
	 */
	private final Expression classNameExpression;

	/**
	 * Constructor.
	 * @param classNameExpression the expression that determines the name of the class to instantiate
	 * @param parameters the constructor parameters
	 */
	public NewExpression(Expression classNameExpression, Expression... parameters) {
		super(parameters);
		this.classNameExpression = classNameExpression;
	}

	/**
	 * Getter method for the classNameExpression.
	 * @return the classNameExpression
	 */
	public Expression getClassNameExpression() {
		return classNameExpression;
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
		dumper.print("new ");
		classNameExpression.dump(dumper);
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
