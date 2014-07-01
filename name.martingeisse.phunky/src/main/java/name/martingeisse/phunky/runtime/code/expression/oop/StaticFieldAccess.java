/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractVariableExpression;
import name.martingeisse.phunky.runtime.variable.Variable;
import org.apache.commons.lang3.NotImplementedException;


/**
 * This expression accesses a static field of a class.
 */
public final class StaticFieldAccess extends AbstractVariableExpression {

	/**
	 * the className
	 */
	private final String className;
	
	/**
	 * the fieldName
	 */
	private final String fieldName;

	/**
	 * Constructor.
	 * @param className the name of the class that contains the field
	 * @param fieldName the name of the field to access
	 */
	public StaticFieldAccess(String className, String fieldName) {
		this.className = Self.normalize(className);
		this.fieldName = fieldName;
	}

	/**
	 * Getter method for the className.
	 * @return the className
	 */
	public String getClassName() {
		return className;
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
