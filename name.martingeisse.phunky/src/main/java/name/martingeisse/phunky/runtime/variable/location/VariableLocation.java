/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.variable.location;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * Denotes the location where a variable is or could be.
 * This is used to specify the target of a value assignment
 * or reference assignment, to allow re-binding to a different
 * variable before the actual assignment occurs. It is also
 * used to specify where a reference to a variable gets stored
 * in case of a reference assignment.
 */
public interface VariableLocation {

	/**
	 * Gets the variable from this location.
	 * 
	 * @param environment the environment
	 * @param codeLocation the location in code, used to trigger errors
	 * @return the variable, or null if it doesn't exist
	 */
	public Variable getVariable(Environment environment, CodeLocation codeLocation);
	
	/**
	 * @param environment the environment
	 * @param codeLocation the location in code, used to trigger errors
	 * @param variable the variable, or null to unset
	 */
	public void setVariable(Environment environment, CodeLocation codeLocation, Variable variable);
	
}
