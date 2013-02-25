/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QMarkers;
import name.martingeisse.webide.resources.MarkerOrigin;
import name.martingeisse.webide.resources.ResourcePath;

import com.mysema.query.sql.dml.SQLDeleteClause;

/**
 * This operation deletes a resource in the workspace.
 */
public final class DeleteMultipleResourcesMarkersOperation extends MultipleResourcesOperation {

	/**
	 * the origin
	 */
	private MarkerOrigin origin;
	
	/**
	 * Constructor.
	 * @param paths the paths of the resources to delete
	 * @param origin the origin, or null to delete markers from all origins
	 */
	public DeleteMultipleResourcesMarkersOperation(Collection<? extends ResourcePath> paths, final MarkerOrigin origin) {
		super(paths);
		this.origin = origin;
	}

	/**
	 * Constructor.
	 * @param paths the paths of the resources to delete
	 * @param origin the origin, or null to delete markers from all origins
	 */
	public DeleteMultipleResourcesMarkersOperation(ResourcePath[] paths, final MarkerOrigin origin) {
		super(paths);
		this.origin = origin;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.WorkspaceOperationContext)
	 */
	@Override
	protected void perform(final WorkspaceOperationContext context) {
		List<String> pathStrings = new ArrayList<String>();
		for (ResourcePath path : getPaths()) {
			pathStrings.add(path.withTrailingSeparator(false).toString());
		}
		trace("will delete resource markers now", getPaths());
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.path.in(pathStrings));
		if (origin != null) {
			delete.where(QMarkers.markers.origin.eq(origin.toString()));
		}
		delete.execute();
	}

}
