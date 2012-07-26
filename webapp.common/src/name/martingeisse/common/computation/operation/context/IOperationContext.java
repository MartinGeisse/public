/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.operation.context;

/**
 * This class allows operations to inherit functionality from their
 * callers (such as database connections).
 */
public interface IOperationContext {

	/**
	 * Returns the context value with the specified type.
	 * @param <T> the type
	 * @param type the class object for the type
	 * @return the context value, or null if not found
	 */
	public <T> T getContextValue(Class<T> type);
	
}
