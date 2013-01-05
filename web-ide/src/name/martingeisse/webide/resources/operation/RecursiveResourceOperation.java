/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
		List<WorkspaceResources> currentResources = fetchRootResources(context);
		while (!currentResources.isEmpty()) {
			List<FetchResourceResult> fetchResults = createFetchResourceResults(currentResources);
			onLevelFetched(fetchResults);
			Set<Long> parentIdsForNextLevel = getContainerResourceIds(fetchResults);
			currentResources = fetchNextLevel(parentIdsForNextLevel);
		}
	}

	/**
	 * Fetches the resources denoted by the root paths.
	 */
	private List<WorkspaceResources> fetchRootResources(final IWorkspaceOperationContext context) {
		return MultipleResourcesOperation.fetchResources(context, rootPaths);
	}
	
	/**
	 * Creates FetchResourceResult instances for subclass code.
	 */
	private List<FetchResourceResult> createFetchResourceResults(List<WorkspaceResources> resources) {
		List<FetchResourceResult> results = new ArrayList<FetchResourceResult>(resources.size());
		for (WorkspaceResources resource : resources) {
			results.add(new FetchResourceResult(path, resource));
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
