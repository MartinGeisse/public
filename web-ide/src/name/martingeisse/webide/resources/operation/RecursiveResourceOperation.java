/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;

import com.mysema.query.sql.SQLQuery;

/**
 * Base class for operations that handle resources recursively,
 * starting at a set of root resources and moving down the resource
 * tree.
 */
public abstract class RecursiveResourceOperation extends WorkspaceOperation {

	/**
	 * the rootPaths
	 */
	private final ResourcePath[] rootPaths;

	/**
	 * Constructor for a single root path.
	 * @param rootPath the root path
	 */
	public RecursiveResourceOperation(final ResourcePath rootPath) {
		this(new ResourcePath[] {
			rootPath
		});
	}

	/**
	 * Constructor for an array of root paths.
	 * @param rootPaths the root paths. This array is not copied; the behavior
	 * of this class is unspecified if the array is modified after calling
	 * this constructor.
	 */
	public RecursiveResourceOperation(final ResourcePath[] rootPaths) {
		this.rootPaths = rootPaths;
	}

	/**
	 * Constructor for a collection of root paths.
	 * @param rootPaths the root paths
	 */
	public RecursiveResourceOperation(final Collection<ResourcePath> rootPaths) {
		this.rootPaths = rootPaths.toArray(new ResourcePath[rootPaths.size()]);
	}

	/**
	 * Getter method for the rootPaths.
	 * @return the rootPaths
	 */
	public ResourcePath[] getRootPaths() {
		return rootPaths;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.IWorkspaceOperationContext)
	 */
	@Override
	protected void perform(final IWorkspaceOperationContext context) {
		
		// prepare -- the special case for a single root is needed to avoid infinite recursion since
		// MultipleResourcesOperation.fetchResources uses a RecursiveResourceOperation too!
		List<WorkspaceResources> currentResources;
		Map<Long, ResourcePath> pathById;
		if (rootPaths.length == 0) {
			return;
		} else if (rootPaths.length == 1) {
			WorkspaceResources rootResource = SingleResourceOperation.fetchResource(context, rootPaths[0]);
			if (rootResource == null) {
				throw new WorkspaceResourceNotFoundException(rootPaths[0]);
			}
			currentResources = new ArrayList<WorkspaceResources>();
			currentResources.add(rootResource);
			pathById = new HashMap<Long, ResourcePath>();
			pathById.put(rootResource.getId(), rootPaths[0]);
		} else {
			currentResources = MultipleResourcesOperation.fetchResources(context, rootPaths);
			pathById = associateRootPaths(currentResources);
		}
		
		// recursive handling
		while (!currentResources.isEmpty()) {
			List<FetchResourceResult> fetchResults = createFetchResourceResults(currentResources, pathById);
			onLevelFetched(fetchResults);
			Set<Long> parentIdsForNextLevel = getContainerResourceIds(fetchResults);
			currentResources = fetchNextLevel(parentIdsForNextLevel);
		}
		
	}
	
	/**
	 * Creates a id-to-path map from the root resources (assumed to be in exactly
	 * the same order as the root paths).
	 */
	private Map<Long, ResourcePath> associateRootPaths(List<WorkspaceResources> rootResources) {
		Map<Long, ResourcePath> pathById = new HashMap<Long, ResourcePath>();
		int i = 0;
		for (WorkspaceResources resource : rootResources) {
			pathById.put(resource.getId(), rootPaths[i]);
			i++;
		}
		return pathById;
	}
	
	/**
	 * Creates FetchResourceResult instances for subclass code. Expects the parent resources to
	 * be present in the pathById map and inserts the specified resources into that map.
	 */
	private List<FetchResourceResult> createFetchResourceResults(List<WorkspaceResources> resources, Map<Long, ResourcePath> pathById) {
		List<FetchResourceResult> results = new ArrayList<FetchResourceResult>(resources.size());
		for (WorkspaceResources resource : resources) {
			ResourcePath resourcePath = pathById.get(resource.getId());
			if (resourcePath == null) {
				ResourcePath parentPath = pathById.get(resource.getParentId());
				if (parentPath == null) {
					throw new RuntimeException("pathById contains no path for id " + resource.getId() + " nor parent id " + resource.getParentId());
				}
				resourcePath = parentPath.appendSegment(resource.getName(), false);
				pathById.put(resource.getId(), resourcePath);
			}
			results.add(new FetchResourceResult(resourcePath, resource));
		}
		return results;
	}
	
	/**
	 * This method is called whenever a "level" of resources has been fetched. Implementations
	 * can, for example, handle the contents of the resources.
	 * 
	 * Implementations are allowed to modify the argument list in a single way, namely removing
	 * resources that should be excluded from further recursive handling. This has, obviously,
	 * no effect for files since they do not have child resources anyway.
	 *  
	 * @param fetchResults the fetch results
	 */
	protected abstract void onLevelFetched(List<FetchResourceResult> fetchResults);

	/**
	 * Returns the IDs for all container resources of the specified fetch results in a set.
	 */
	private Set<Long> getContainerResourceIds(List<FetchResourceResult> fetchResults) {
		HashSet<Long> ids = new HashSet<Long>(fetchResults.size());
		for (FetchResourceResult fetchResult : fetchResults) {
			if (fetchResult.getType() != ResourceType.FILE) {
				ids.add(fetchResult.getId());
			}
		}
		return ids;
	}

	/**
	 * Fetches the next level of resources using the parent IDs.
	 */
	private List<WorkspaceResources> fetchNextLevel(Set<Long> parentIds) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QWorkspaceResources.workspaceResources);
		query.where(QWorkspaceResources.workspaceResources.parentId.in(parentIds));
		return query.list(QWorkspaceResources.workspaceResources);
	}
	
}
