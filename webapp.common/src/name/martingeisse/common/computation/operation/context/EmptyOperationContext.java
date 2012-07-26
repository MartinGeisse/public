/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.operation.context;

/**
 * Empty implementation of {@link IOperationContext}. This class can
 * be used to provide an operation with an empty context if no
 * context is available.
 */
public class EmptyOperationContext implements IOperationContext {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.IOperationContext#getContextValue(java.lang.Class)
	 */
	@Override
	public <T> T getContextValue(Class<T> type) {
		return null;
	}

}
