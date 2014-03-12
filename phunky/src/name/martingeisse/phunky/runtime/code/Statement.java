/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code;

import name.martingeisse.phunky.runtime.Environment;

/**
 * A statement is an object that can be executed using an
 * {@link Environment}, for side effects on the environment
 * and on other objects.
 */
public interface Statement {

	/**
	 * Executes this statement.
	 * @param environment the environment
	 */
	public void execute(Environment environment);
	
}
