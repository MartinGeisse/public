/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

/**
 * An exception that can be thrown during workspace operations.
 */
public class WorkspaceOperationException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public WorkspaceOperationException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public WorkspaceOperationException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public WorkspaceOperationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public WorkspaceOperationException(String message, Throwable cause) {
		super(message, cause);
	}

}
