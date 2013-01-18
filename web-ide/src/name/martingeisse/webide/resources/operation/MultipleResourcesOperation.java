/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import name.martingeisse.webide.resources.ResourcePath;

/**
 * Internal base implementation for operations that affect multiple
 * resources specified by multiple paths.
 */
public abstract class MultipleResourcesOperation extends WorkspaceOperation {

	/**
	 * the paths
	 */
	private final ResourcePath[] paths;

	/**
	 * Constructor.
	 * @param paths the paths of the resources
	 */
	public MultipleResourcesOperation(final ResourcePath[] paths) {
		this.paths = paths;
	}
	
	/**
	 * Constructor.
	 * @param paths the paths of the resources
	 */
	public MultipleResourcesOperation(final Collection<? extends ResourcePath> paths) {
		this.paths = paths.toArray(new ResourcePath[paths.size()]);
	}

	/**
	 * Getter method for the paths.
	 * @return the paths
	 */
	public ResourcePath[] getPaths() {
		return paths;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#logAndPerform(name.martingeisse.webide.resources.operation.WorkspaceOperationContext)
	 */
	@Override
	void logAndPerform(WorkspaceOperationContext context) {
		if (isDebugEnabled()) {
			debug(getClass().getSimpleName() + " begin", paths);
		}
		perform(context);
		if (isDebugEnabled()) {
			debug(getClass().getSimpleName() + " end", paths);
		}
	}
	
	/**
	 * Obtains and returns the ID of the resources, possibly querying the database.
	 * IDs are returned in exactly the same order as the paths.
	 */
	protected final List<Long> fetchResourceIds(WorkspaceOperationContext context) {
		ArrayList<Long> ids = new ArrayList<Long>();
		for (FetchResourceResult resource : fetchResources(context)) {
			ids.add(resource.getId());
		}
		return ids;
	}

	/**
	 * Obtains and returns the resources from the database.
	 * Resources are returned in exactly the same order as the paths.
	 */
	protected final List<FetchResourceResult> fetchResources(WorkspaceOperationContext context) {
		return context.fetchResources(paths);
	}
	
}
