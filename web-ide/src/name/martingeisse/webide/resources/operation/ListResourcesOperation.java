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

import com.mysema.query.sql.SQLQuery;

/**
 * This operation lists the children of the specified parent resource.
 */
public class ListResourcesOperation extends SingleResourceOperation {

	/**
	 * the children
	 */
	private List<FetchResourceResult> children;

	/**
	 * Constructor.
	 * @param path the path of the parent resource
	 */
	public ListResourcesOperation(final ResourcePath path) {
		super(path);
	}

	/**
	 * Getter method for the children.
	 * @return the children
	 */
	public List<FetchResourceResult> getChildren() {
		return children;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.WorkspaceOperationContext)
	 */
	@Override
	protected void perform(final WorkspaceOperationContext context) {
		children = null;
		final long parentId = fetchResourceId(context);
		if (parentId == -1) {
			throw new WorkspaceResourceNotFoundException(getPath());
		}
		trace("will list resources now", getPath());
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QWorkspaceResources.workspaceResources);
		query.where(QWorkspaceResources.workspaceResources.parentId.eq(parentId));
		final List<WorkspaceResources> resources = query.list(QWorkspaceResources.workspaceResources);
		children = new ArrayList<FetchResourceResult>(resources.size());
		for (final WorkspaceResources resource : resources) {
			final ResourcePath path = getPath().appendSegment(resource.getName(), false);
			children.add(new FetchResourceResult(path, resource));
		}
	}

}
