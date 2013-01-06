/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

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
	 * Obtains and returns the ID of the resource, possibly querying the database.
	 */
	List<Long> fetchResourceIds(IWorkspaceOperationContext context) {
		return fetchResourceIds(context, paths);
	}

	/**
	 * Obtains and returns the ID of the resource, possibly querying the database.
	 */
	static List<Long> fetchResourceIds(IWorkspaceOperationContext context, ResourcePath[] paths) {
		// TODO
		throw new NotImplementedException();
	}

	/**
	 * Obtains and returns the resource from the database.
	 */
	List<WorkspaceResources> fetchResource(IWorkspaceOperationContext context) {
		return fetchResources(context, paths);
	}
	
	/**
	 * Obtains and returns the resource from the database.
	 */
	static List<WorkspaceResources> fetchResources(IWorkspaceOperationContext context, ResourcePath[] paths) {
		// TODO
		throw new NotImplementedException();
	}
	
}
