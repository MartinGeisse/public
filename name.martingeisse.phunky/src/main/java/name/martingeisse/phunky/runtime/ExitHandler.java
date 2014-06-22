/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

import name.martingeisse.phunky.runtime.builtin.system.DieFunction;

/**
 * This class can be registered with the {@link PhpRuntime} to react
 * to the script exiting.
 */
public interface ExitHandler {

	/**
	 * This method is called when the script exits by returning normally
	 * or executing past the last line of code.
	 */
	public void onNormalExit();
	
	/**
	 * This method is called when the script exits explicitly by calling an
	 * appropriate function such as the {@link DieFunction}.
	 * 
	 * @param statusCode the status code passed to the exit function
	 */
	public void onExplicitExit(int statusCode);

	/**
	 * This method is called when the script exits because it encountered
	 * a fatal error.
	 */
	public void onFatalError();

	/**
	 * This method is called when the script exits because it threw
	 * an uncaught exception.
	 * 
	 * @param exception the exception
	 */
	public void onUncaughtException(Throwable exception);

}
