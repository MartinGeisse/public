/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;

import com.mysema.query.sql.dml.SQLDeleteClause;

/**
 * This operation deletes a resource in the workspace.
 */
public final class DeleteResourceOperation extends SingleResourceOperation {

	/**
	 * Constructor.
	 * @param path the path of the file
	 */
	public DeleteResourceOperation(final ResourcePath path) {
		super(path);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.WorkspaceOperationContext)
	 */
	@Override
	protected void perform(final WorkspaceOperationContext context) {
		// Child resources are deleted by the SQL cascade settings. Note that we
		// specify the resource to be deleted by id rather than by parent id and
		// name -- although this causes an extra SELECT, it ensures that the
		// resource exists rather than fail silently for missing resources.
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QWorkspaceResources.workspaceResources);
		delete.where(QWorkspaceResources.workspaceResources.id.eq(fetchResourceId(context)));
		trace("will delete resource now", getPath());
		delete.execute();
		WorkspaceResourceDeltaUtil.generateDeltas("delete resource", getPath());
		WorkspaceCache.invalidate();
	}

}
