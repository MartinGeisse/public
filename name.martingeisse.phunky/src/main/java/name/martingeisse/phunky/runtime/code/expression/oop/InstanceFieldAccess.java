/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.assignment.AssignmentTarget;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractVariableExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;
import name.martingeisse.phunky.runtime.variable.Variable;

import org.apache.commons.lang3.NotImplementedException;

/**
 * This expression accesses a field of an object.
 */
public final class InstanceFieldAccess extends AbstractVariableExpression {

	/**
	 * the objectExpression
	 */
	private final Expression objectExpression;

	/**
	 * the fieldNameExpression
	 */
	private final Expression fieldNameExpression;

	/**
	 * Constructor.
	 * @param objectExpression the expression that determines the object that contains the field
	 * @param fieldNameExpression the expression that determines the name of the field to access
	 */
	public InstanceFieldAccess(Expression objectExpression, Expression fieldNameExpression) {
		this.objectExpression = objectExpression;
		this.fieldNameExpression = fieldNameExpression;
	}

	/**
	 * Getter method for the objectExpression.
	 * @return the objectExpression
	 */
	public Expression getObjectExpression() {
		return objectExpression;
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
	public Variable resolveVariable(Environment environment) {
		// TODO
		throw new NotImplementedException("");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getOrCreateVariable(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Variable resolveOrCreateVariable(Environment environment) {
		// TODO
		throw new NotImplementedException("");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#resolveValueAcceptor(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public AssignmentTarget resolveValueAcceptor(Environment environment) {
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
