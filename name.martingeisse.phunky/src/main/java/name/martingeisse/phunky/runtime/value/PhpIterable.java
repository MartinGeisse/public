/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.value;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.statement.Statement;

/**
 * Implemented by native objects that can be used for "foreach" iteration in PHP.
 */
public interface PhpIterable {

	/**
	 * Iterates over the elements of this object. For each element, the iteration variables are bound, then
	 * the body is executed.
	 * 
	 * @param environment the environment
	 * @param keyIterationVariableName the name of the key iteration variable, or null for none
	 * @param valueIterationVariableName the name of the value iteration variable, or null for none
	 * @param body the body to execute
	 */
	public void iterate(Environment environment, String keyIterationVariableName, String valueIterationVariableName, Statement body);
	
}
