/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;

import com.mysema.query.sql.SQLQuery;

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
	final long fetchResourceId(IWorkspaceOperationContext context) {
		return fetchResourceId(context, path);
	}

	/**
	 * Obtains and returns the ID of the resource, possibly querying the database.
	 * Returns -1 if the resource cannot be found.
	 */
	static long fetchResourceId(IWorkspaceOperationContext context, ResourcePath path) {
		WorkspaceResources resource = fetchResource(context, path);
		return (resource == null ? -1 : resource.getId());
	}

	/**
	 * Obtains and returns the resource from the database, or null if the resource
	 * cannot be found.
	 */
	final WorkspaceResources fetchResource(IWorkspaceOperationContext context) {
		return fetchResource(context, path);
	}
	
	/**
	 * Obtains and returns the resource from the database, or null if the resource
	 * cannot be found.
	 */
	static WorkspaceResources fetchResource(IWorkspaceOperationContext context, ResourcePath path) {
		if (!path.isLeadingSeparator()) {
			throw new WorkspaceOperationException("cannot fetch resource using a relative path: " + path);
		}
		if (path.getSegmentCount() == 0) {
			final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
			query.from(QWorkspaceResources.workspaceResources);
			query.where(QWorkspaceResources.workspaceResources.type.eq(ResourceType.WORKSPACE_ROOT.name()));
			return query.singleResult(QWorkspaceResources.workspaceResources);
		}
		WorkspaceResources parent = fetchResource(context, path.removeLastSegment(false));
		if (parent == null) {
			return null;
		}
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QWorkspaceResources.workspaceResources);
		query.where(QWorkspaceResources.workspaceResources.parentId.eq(parent.getId()));
		return query.singleResult(QWorkspaceResources.workspaceResources);
	}
	
}
