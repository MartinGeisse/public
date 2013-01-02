/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

/**
 * An exception that indicates a missing workspace resource.
 */
public class WorkspaceResourceNotFoundException extends WorkspaceOperationException {

	/**
	 * Constructor.
	 */
	public WorkspaceResourceNotFoundException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public WorkspaceResourceNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public WorkspaceResourceNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public WorkspaceResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
