/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.common;

/**
 * An abstract action that can be executed and returns a result of
 * a specific type. Any parameters must be set before execution by
 * using a specific subtype of this type.
 * @param <R> the result type
 */
public interface IAction<R> {

	/**
	 * Executes this action.
	 * @return the result
	 */
	public R execute();
	
}
