/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.webide.resources.ResourcePath;

/**
 * An exception that indicates a collision between workspace
 * resources, usually between an existing resource and a
 * to-be-created resource with the same parent and name.
 */
public class WorkspaceResourceCollisionException extends WorkspaceOperationException {

	/**
	 * Constructor.
	 * @param path the path of the missing resource
	 */
	public WorkspaceResourceCollisionException(ResourcePath path) {
		super("resource collision: " + path);
	}

	/**
	 * Constructor.
	 * @param path the path of the missing resource
	 * @param message the exception message
	 */
	public WorkspaceResourceCollisionException(ResourcePath path, String message) {
		super("resource collision: " + path + ": " + message);
	}

	/**
	 * Constructor.
	 * @param path the path of the missing resource
	 * @param cause the exception that caused this exception
	 */
	public WorkspaceResourceCollisionException(ResourcePath path, Throwable cause) {
		super("resource collision: " + path, cause);
	}

	/**
	 * Constructor.
	 * @param path the path of the missing resource
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public WorkspaceResourceCollisionException(ResourcePath path, String message, Throwable cause) {
		super("resource collision: " + path + ": " + message, cause);
	}

}
