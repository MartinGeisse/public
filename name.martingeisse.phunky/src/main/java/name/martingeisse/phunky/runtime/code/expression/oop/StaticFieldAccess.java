/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractVariableExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;
import name.martingeisse.phunky.runtime.variable.Variable;

import org.apache.commons.lang3.NotImplementedException;


/**
 * This expression accesses a static field of a class.
 */
public final class StaticFieldAccess extends AbstractVariableExpression {

	/**
	 * the classNameExpression
	 */
	private final Expression classNameExpression;
	
	/**
	 * the fieldNameExpression
	 */
	private final Expression fieldNameExpression;

	/**
	 * Constructor.
	 * @param classNameExpression the expression that determines the name of the class that contains the field
	 * @param fieldNameExpression the expression that determines the name of the field to access
	 */
	public StaticFieldAccess(Expression classNameExpression, Expression fieldNameExpression) {
		this.classNameExpression = classNameExpression;
		this.fieldNameExpression = fieldNameExpression;
	}

	/**
	 * Getter method for the classNameExpression.
	 * @return the classNameExpression
	 */
	public Expression getClassNameExpression() {
		return classNameExpression;
	}
	
	/**
	 * Getter method for the fieldNameExpression.
	 * @return the fieldNameExpression
	 */
	public Expression getFieldNameExpression() {
		return fieldNameExpression;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getVariable(Environment environment) {
		// TODO
		throw new NotImplementedException("");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable getOrCreateVariable(Environment environment) {
		// TODO
		throw new NotImplementedException("");
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#bindVariableReference(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.variable.Variable)
	 */
	@Override
	public void bindVariableReference(Environment environment, Variable variable) {
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
		fieldNameExpression.dump(dumper);
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
