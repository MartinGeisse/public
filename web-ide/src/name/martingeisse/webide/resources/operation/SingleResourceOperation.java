/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;

/**
 * Internal base implementation for operations that affect a single resource
 * specified by path.
 */
public abstract class SingleResourceOperation extends WorkspaceOperation {

	/**
	 * the path
	 */
	private final ResourcePath path;

	/**
	 * Constructor.
	 * @param path the path of the resource
	 */
	public SingleResourceOperation(final ResourcePath path) {
		this.path = path;
	}

	/**
	 * Getter method for the path.
	 * @return the path
	 */
	public ResourcePath getPath() {
		return path;
	}
	
	/**
	 * Obtains and returns the ID of the resource, possibly querying the database.
	 */
	long fetchResourceId(IWorkspaceOperationContext context) {
		return fetchResourceId(context, path);
	}

	/**
	 * Obtains and returns the ID of the resource, possibly querying the database.
	 * Returns -1 if the resource cannot be found.
	 */
	static long fetchResourceId(IWorkspaceOperationContext context, ResourcePath path) {
		
	}

	/**
	 * Obtains and returns the resource from the database, or null if the resource
	 * cannot be found.
	 */
	WorkspaceResources fetchResource(IWorkspaceOperationContext context) {
		return fetchResource(context, path);
	}
	
	/**
	 * Obtains and returns the resource from the database, or null if the resource
	 * cannot be found.
	 */
	static WorkspaceResources fetchResource(IWorkspaceOperationContext context, ResourcePath path) {
		
	}
	
}
