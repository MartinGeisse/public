/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.variable;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.statement.Statement;

/**
 * Allows to iterate over the values contained in this value.
 */
public interface PhpValueIterationProvider {

	/**
	 * Iterates over the elements from this provider. For each element, the iteration variables are bound, then
	 * the body is executed.
	 * 
	 * @param environment the environment
	 * @param keyIterationVariableName the name of the key iteration variable, or null for none
	 * @param valueIterationVariableName the name of the value iteration variable, or null for none
	 * @param body the body to execute
	 */
	public void iterate(final Environment environment, final String keyIterationVariableName, final String valueIterationVariableName, final Statement body);

}
