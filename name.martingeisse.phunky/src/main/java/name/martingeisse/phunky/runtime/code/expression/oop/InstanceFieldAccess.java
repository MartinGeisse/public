/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractVariableExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
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
	 * the fieldName
	 */
	private final String fieldName;

	/**
	 * Constructor.
	 * @param objectExpression the expression that determines the object that contains the field
	 * @param fieldName the name of the field to access
	 */
	public InstanceFieldAccess(Expression objectExpression, String fieldName) {
		this.objectExpression = objectExpression;
		this.fieldName = fieldName;
	}

	/**
	 * Getter method for the objectExpression.
	 * @return the objectExpression
	 */
	public Expression getObjectExpression() {
		return objectExpression;
	}

	/**
	 * Getter method for the fieldName.
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
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
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		// TODO
		throw new NotImplementedException("");
	}
	
}
