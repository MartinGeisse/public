/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

/**
 * This interface is implemented by any object that can handle being
 * called as a function.
 */
public interface Callable {

	/**
	 * Calls this object.
	 * @param arguments the argument values
	 * @return the return value
	 */
	public Object call(Object[] arguments);
	
}
