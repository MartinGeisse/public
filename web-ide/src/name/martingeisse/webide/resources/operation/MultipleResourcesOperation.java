/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import name.martingeisse.webide.entity.WorkspaceResources;
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
	
	/**
	 * Obtains and returns the ID of the resources, possibly querying the database.
	 * IDs are returned in exactly the same order as the paths.
	 */
	List<Long> fetchResourceIds(IWorkspaceOperationContext context) {
		return fetchResourceIds(context, paths);
	}

	/**
	 * Obtains and returns the ID of the resources, possibly querying the database.
	 * IDs are returned in exactly the same order as the paths.
	 */
	static List<Long> fetchResourceIds(IWorkspaceOperationContext context, ResourcePath[] paths) {
		ArrayList<Long> ids = new ArrayList<Long>();
		for (WorkspaceResources resource : fetchResources(context, paths)) {
			ids.add(resource.getId());
		}
		return ids;
	}

	/**
	 * Obtains and returns the resources from the database.
	 * Resources are returned in exactly the same order as the paths.
	 */
	List<WorkspaceResources> fetchResource(IWorkspaceOperationContext context) {
		return fetchResources(context, paths);
	}
	
	/**
	 * Obtains and returns the resources from the database.
	 * Resources are returned in exactly the same order as the paths.
	 */
	static List<WorkspaceResources> fetchResources(IWorkspaceOperationContext context, ResourcePath[] paths) {
		List<WorkspaceResources> results = new ArrayList<WorkspaceResources>();
		for (ResourcePath path : paths) {
			results.add(SingleResourceOperation.fetchResource(context, path));
		}
		return results;

		// TODO: optimize!!!
		// ResourcePath[] remainingPaths = Arrays.copyOf(paths, paths.length);
		// WorkspaceResources[] resources = new WorkspaceResources[paths.length];
	}
	
}
