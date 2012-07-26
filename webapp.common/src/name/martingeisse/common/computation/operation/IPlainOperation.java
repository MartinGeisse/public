/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.operation;

import name.martingeisse.common.computation.operation.context.IOperationContext;

/**
 * An operation that can be executed without any further information.
 * Any input values must be set using appropriate sub-interfaces
 * before execution. This interface does support a return value though.
 * 
 * The operation context can be used to provide the operation with
 * resources it needs without passing them around all the time. This
 * is especially useful with resources such as database connections
 * which are cumbersome to pass through intermediate helper operations
 * that handle batch processing, logging, exception handling etc.
 * 
 * @param <T> the result type
 */
public interface IPlainOperation<T> {

	/**
	 * Executes this operation.
	 * @param context the context
	 * @return the result
	 */
	public T execute(IOperationContext context);
	
}
