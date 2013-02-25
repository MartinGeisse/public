/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;


/**
 * An exception that indicates a missing workspace resource.
 */
public class WorkspaceResourceNotFoundException extends WorkspaceOperationException {

	/**
	 * Constructor.
	 * @param path the path of the missing resource
	 */
	public WorkspaceResourceNotFoundException(ResourcePath path) {
		super("resource not found: " + path);
	}

	/**
	 * Constructor.
	 * @param path the path of the missing resource
	 * @param message the exception message
	 */
	public WorkspaceResourceNotFoundException(ResourcePath path, String message) {
		super("resource not found: " + path + ": " + message);
	}

	/**
	 * Constructor.
	 * @param path the path of the missing resource
	 * @param cause the exception that caused this exception
	 */
	public WorkspaceResourceNotFoundException(ResourcePath path, Throwable cause) {
		super("resource not found: " + path, cause);
	}

	/**
	 * Constructor.
	 * @param path the path of the missing resource
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public WorkspaceResourceNotFoundException(ResourcePath path, String message, Throwable cause) {
		super("resource not found: " + path + ": " + message, cause);
	}

}
