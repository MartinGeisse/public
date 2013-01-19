/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import name.martingeisse.webide.resources.ResourcePath;


/**
 * Encapsulates all caching functionality for database records
 * of workspace resources. Workspace operations and the operation
 * context access this class through static methods.
 * 
 * Note: The cache is not synchronized beyond structural integrity
 * of the {@link ConcurrentMap}s. Doing so properly would also require
 * integrity of the whole workspace across concurrent operations,
 * at least while resource IDs are stored by operations (even for
 * operations that just use IDs to associate resources -- they would be
 * confused if IDs suddenly change).
 * 
 * TODO: does not handle multiple workspaces yet
 */
class WorkspaceCache {

	/**
	 * the resourcesById
	 */
	private static ConcurrentMap<Long, FetchResourceResult> resourcesById = new ConcurrentHashMap<Long, FetchResourceResult>();

	/**
	 * the resourcesByParentId
	 */
	private static ConcurrentMap<Long, List<FetchResourceResult>> resourcesByParentId = new ConcurrentHashMap<Long, List<FetchResourceResult>>();

	/**
	 * the resourcesByPath
	 */
	private static ConcurrentMap<ResourcePath, FetchResourceResult> resourcesByPath = new ConcurrentHashMap<ResourcePath, FetchResourceResult>();
	
	/**
	 * Prevent instantiation.
	 */
	private WorkspaceCache() {
	}

	/**
	 * Updates the cache after a resource was created in the database.
	 * @param fetchResult the fetch result to store
	 */
	static void onCreate(long id, long parentId, ResourcePath path) {
		resourcesById.remove(id);
		resourcesByParentId.remove(parentId);
		resourcesByPath.remove(path);
	}

	/**
	 * Updates the cache after a resource was loaded from the database.
	 * @param fetchResult the fetch result to store
	 */
	static void onLoad(FetchResourceResult fetchResult) {
		resourcesById.put(fetchResult.getId(), fetchResult);
		resourcesByPath.put(fetchResult.getPath(), fetchResult);
	}

	/**
	 * Last-resort method for modifications that cannot otherwise be reconciled
	 * by this cache -- evicts all cached data.
	 */
	static void invalidate() {
		resourcesById.clear();
		resourcesByParentId.clear();
		resourcesByPath.clear();
	}

	/**
	 * Returns a cached resource by id.
	 * @param id the resource id
	 * @return the fetch result, or null if not cached
	 */
	static FetchResourceResult getResourceById(long id) {
		return resourcesById.get(id);
	}

	/**
	 * Returns cached resources by parent id. If this method returns a result, it is
	 * the exact list of child resources for that parent id, i.e. any modification
	 * that might cause the list to be incomplete or otherwise incorrect will remove
	 * it from the cache.
	 * 
	 * @param id the resource id
	 * @return the fetch result, or null if not cached
	 */
	static List<FetchResourceResult> resourcesByParentId(long parentId) {
		return resourcesByParentId.get(parentId);
	}
	
	/**
	 * Returns a cached resource by path.
	 * @param path the resource path
	 * @return the fetch result, or null if not cached
	 */
	static FetchResourceResult getResourceByPath(ResourcePath path) {
		return resourcesByPath.get(path);
	}
	
}
