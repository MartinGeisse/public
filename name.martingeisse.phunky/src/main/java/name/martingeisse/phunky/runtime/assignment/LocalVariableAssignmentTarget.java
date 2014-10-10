/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.assignment;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * {@link AssignmentTarget} implementation for local variables.
 */
public final class LocalVariableAssignmentTarget implements AssignmentTarget {

	/**
	 * the environment
	 */
	private final Environment environment;

	/**
	 * the name
	 */
	private final String name;

	/**
	 * Constructor.
	 * @param environment the environment that contains the local variable
	 * @param name the variable name
	 */
	public LocalVariableAssignmentTarget(Environment environment, String name) {
		this.environment = environment;
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#getValue()
	 */
	@Override
	public Object getValue() {
		return environment.get(name).getValue();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignValue(java.lang.Object)
	 */
	@Override
	public void assignValue(Object value) {
		environment.get(name).setValue(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignReference(name.martingeisse.phunky.runtime.variable.Variable)
	 */
	@Override
	public void assignReference(Variable target) {
		environment.put(name, target);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#unset()
	 */
	@Override
	public void unset() {
		environment.remove(name);
	}

}
