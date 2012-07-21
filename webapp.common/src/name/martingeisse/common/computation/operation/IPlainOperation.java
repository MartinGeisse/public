/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.operation;

/**
 * An operation that can be executed without any further information.
 * Any input values must be set using appropriate sub-interfaces
 * before execution. This interface does support a return value though.
 * 
 * @param <T> the result type
 */
public interface IPlainOperation<T> {

	/**
	 * Executes this operation.
	 * @return the result
	 */
	public T execute();
	
}
