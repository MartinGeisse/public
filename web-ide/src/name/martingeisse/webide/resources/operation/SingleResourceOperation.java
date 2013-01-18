/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.webide.resources.ResourcePath;

import org.apache.log4j.Logger;

/**
 * Internal base implementation for operations that affect a single resource
 * specified by path.
 */
public abstract class SingleResourceOperation extends WorkspaceOperation {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SingleResourceOperation.class);
	
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
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#logAndPerform(name.martingeisse.webide.resources.operation.WorkspaceOperationContext)
	 */
	@Override
	void logAndPerform(WorkspaceOperationContext context) {
		if (isDebugEnabled()) {
			debug(getClass().getSimpleName() + " begin", path);
		}
		perform(context);
		if (isDebugEnabled()) {
			debug(getClass().getSimpleName() + " end", path);
		}
	}
	
	/**
	 * Obtains and returns the ID of the resource, possibly querying the database.
	 * Returns -1 if the resource cannot be found.
	 */
	protected final long fetchResourceId(WorkspaceOperationContext context) {
		FetchResourceResult result = context.fetchResource(path);
		return (result == null ? -1 : result.getId());
	}

	/**
	 * Obtains and returns the resource from the database, or null if the resource
	 * cannot be found.
	 */
	protected final FetchResourceResult fetchResource(WorkspaceOperationContext context) {
		return context.fetchResource(path);
	}
	
}
