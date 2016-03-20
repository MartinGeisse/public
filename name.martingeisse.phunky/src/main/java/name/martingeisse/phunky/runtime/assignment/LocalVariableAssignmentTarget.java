/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.assignment;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeLocation;
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
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#getValue(name.martingeisse.phunky.runtime.code.CodeLocation)
	 */
	@Override
	public Object getValue(CodeLocation location) {
		Variable variable = environment.get(name);
		if (variable == null) {
			environment.getRuntime().triggerError("no such local variable: " + name, location);
			return null;
		} else {
			return variable.getValue();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignValue(name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object)
	 */
	@Override
	public void assignValue(CodeLocation location, Object value) {
		environment.getOrCreate(name).setValue(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignReference(name.martingeisse.phunky.runtime.code.CodeLocation, name.martingeisse.phunky.runtime.variable.Variable)
	 */
	@Override
	public void assignReference(CodeLocation location, Variable target) {
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
