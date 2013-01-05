/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;

/**
 * Fetches resource information for a single resource and makes them
 * available as a {@link FetchResourceResult}. The result is null if
 * the resource was not found.
 */
public final class FetchSingleResourceOperation extends SingleResourceOperation {

	/**
	 * the result
	 */
	private FetchResourceResult result;
	
	/**
	 * Constructor.
	 * @param path the path of the resource
	 */
	public FetchSingleResourceOperation(final ResourcePath path) {
		super(path);
	}

	/**
	 * Getter method for the result.
	 * @return the result
	 */
	public FetchResourceResult getResult() {
		return result;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.IWorkspaceOperationContext)
	 */
	@Override
	protected void perform(IWorkspaceOperationContext context) {
		WorkspaceResources resource = fetchResource(context);
		this.result = (resource == null ? null : new FetchResourceResult(getPath(), resource));
	}
	
}
