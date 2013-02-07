/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mysema.query.sql.SQLQuery;

/**
 * This class is used by workspace operations to perform actual
 * workspace modifications. An instance is passed to
 * WorkspaceOperation.perform(WorkspaceOperationContext)
 * after calling {@link WorkspaceOperation#run()}.
 */
public final class WorkspaceOperationContext {

	/**
	 * the workspaceId
	 */
	private final long workspaceId;
	
	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(WorkspaceOperationContext.class);
	
	/**
	 * Constructor.
	 * @param workspaceId the ID of the workspace in which the operation(s) are performed
	 */
	WorkspaceOperationContext(long workspaceId) {
		this.workspaceId = workspaceId;
	}

	/**
	 * Getter method for the workspaceId.
	 * @return the workspaceId
	 */
	long getWorkspaceId() {
		return workspaceId;
	}
	
	/**
	 * Disposes of all resources held by this context.
	 */
	void dispose() {
	}

	/**
	 * Checks whether DEBUG-level logging is enabled.
	 */
	static final boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	
	/**
	 * Logs the specified path at DEBUG level (optimized).
	 * @param prefix the prefix to log
	 * @param path the path to log
	 */
	static final void debug(String prefix, ResourcePath path) {
		if (logger.isDebugEnabled()) {
			logger.debug(prefix + ": " + path);
		}
	}
	
	/**
	 * Logs the specified path at DEBUG level (optimized).
	 * @param prefix the prefix to log
	 * @param paths the paths to log
	 */
	static final void debug(String prefix, ResourcePath[] paths) {
		if (logger.isDebugEnabled()) {
			logger.debug(prefix + ": " + StringUtils.join(paths, ", "));
		}
	}
	
	/**
	 * Checks whether TRACE-level logging is enabled.
	 */
	static final boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}
	
	/**
	 * Logs the specified path at TRACE level (optimized).
	 * @param prefix the prefix to log
	 * @param path the path to log
	 */
	static final void trace(String prefix, ResourcePath path) {
		if (logger.isTraceEnabled()) {
			logger.trace(prefix + ": " + path);
		}
	}
	
	/**
	 * Logs the specified path at TRACE level (optimized).
	 * @param prefix the prefix to log
	 * @param paths the paths to log
	 */
	static final void trace(String prefix, ResourcePath[] paths) {
		if (logger.isTraceEnabled()) {
			logger.trace(prefix + ": " + StringUtils.join(paths, ", "));
		}
	}
	
	/**
	 * Fetches a single workspace resource.
	 * @param path the resource path
	 * @return the resource fetch result
	 */
	public FetchResourceResult fetchResource(ResourcePath path) {
		trace("fetching resource ...", path);
		FetchResourceResult cachedResult = WorkspaceCache.getResourceByPath(path);
		if (cachedResult != null) {
			trace("resource was cached", path);
			return cachedResult;
		}
		if (!path.isLeadingSeparator()) {
			throw new WorkspaceOperationException("cannot fetch resource using a relative path: " + path);
		}
		if (path.getSegmentCount() == 0) {
			trace("... is workspace root", path);
			final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
			query.from(QWorkspaceResources.workspaceResources);
			query.where(QWorkspaceResources.workspaceResources.type.eq(ResourceType.FOLDER.name()));
			query.where(QWorkspaceResources.workspaceResources.parentId.isNull());
			FetchResourceResult result = createResult(path, query.singleResult(QWorkspaceResources.workspaceResources));
			if (result != null) {
				WorkspaceCache.onLoad(result);
			}
			return result;
		}
		trace("fetching parent", path);
		FetchResourceResult parent = fetchResource(path.removeLastSegment(false));
		if (parent == null) {
			trace("parent not found", path);
			return null;
		}
		trace("fetching child", path);
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QWorkspaceResources.workspaceResources);
		query.where(QWorkspaceResources.workspaceResources.parentId.eq(parent.getId()));
		query.where(QWorkspaceResources.workspaceResources.name.eq(path.getLastSegment()));
		FetchResourceResult result = createResult(path, query.singleResult(QWorkspaceResources.workspaceResources));
		if (result != null) {
			WorkspaceCache.onLoad(result);
		}
		return result;
	}

	/**
	 * Fetches multiple workspace resources.
	 * Resources are returned in exactly the same order as the paths.
	 * @param paths the resource paths
	 * @return the resource fetch results
	 */
	public List<FetchResourceResult> fetchResources(ResourcePath[] paths) {
		trace("fetching resources", paths);
		List<FetchResourceResult> results = new ArrayList<FetchResourceResult>();
		for (ResourcePath path : paths) {
			results.add(fetchResource(path));
		}
		trace("fetched resources", paths);
		return results;

		// TODO: optimize!!!
		// ResourcePath[] remainingPaths = Arrays.copyOf(paths, paths.length);
		// WorkspaceResources[] resources = new WorkspaceResources[paths.length];
	}
	
	/**
	 * Returns a FetchResourceResult, or null if the resource is null.
	 */
	private static FetchResourceResult createResult(ResourcePath path, WorkspaceResources resource) {
		if (resource == null) {
			return null;
		} else {
			return new FetchResourceResult(path, resource);
		}
	}
	
}
