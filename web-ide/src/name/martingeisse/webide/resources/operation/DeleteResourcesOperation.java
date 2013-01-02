/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.Collection;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;

import com.mysema.query.sql.dml.SQLDeleteClause;

/**
 * This operation deletes a resource in the workspace.
 */
public final class DeleteResourcesOperation extends MultipleResourcesOperation {

	
	/**
	 * Constructor.
	 * @param paths the paths of the resources to delete
	 */
	public DeleteResourcesOperation(Collection<? extends ResourcePath> paths) {
		super(paths);
	}

	/**
	 * Constructor.
	 * @param paths the paths of the resources to delete
	 */
	public DeleteResourcesOperation(ResourcePath[] paths) {
		super(paths);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.IWorkspaceOperationContext)
	 */
	@Override
	protected void perform(final IWorkspaceOperationContext context) {
		// Child resources are deleted by the SQL cascade settings. Note that we
		// specify the resources to be deleted by id rather than by parent id and
		// name -- although this causes an extra SELECT, it ensures that the
		// resources exists rather than fail silently for missing resources.
		List<Long> resourceIds = fetchResourceIds(context);
		if (resourceIds.size() < getPaths().length) {
			throw new WorkspaceOperationException("missing resource or duplicate path");
		}
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QWorkspaceResources.workspaceResources);
		delete.where(QWorkspaceResources.workspaceResources.id.in(resourceIds));
		delete.execute();
	}

}
