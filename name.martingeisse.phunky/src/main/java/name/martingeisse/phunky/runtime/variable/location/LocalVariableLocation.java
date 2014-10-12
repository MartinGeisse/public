/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.variable.location;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * {@link VariableLocation} implementation for local variables.
 */
public final class LocalVariableLocation implements VariableLocation {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * Constructor.
	 * @param name the variable name
	 */
	public LocalVariableLocation(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.variable.location.VariableLocation#getVariable(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.CodeLocation)
	 */
	@Override
	public Variable getVariable(Environment environment, CodeLocation codeLocation) {
		return environment.get(name);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.variable.location.VariableLocation#setVariable(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.CodeLocation, name.martingeisse.phunky.runtime.variable.Variable)
	 */
	@Override
	public void setVariable(Environment environment, CodeLocation codeLocation, Variable variable) {
		if (variable == null) {
			environment.remove(name);
		} else {
			environment.put(name, variable);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "$" + name;
	}

}
