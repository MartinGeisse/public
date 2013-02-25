/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QMarkers;
import name.martingeisse.webide.resources.MarkerOrigin;
import name.martingeisse.webide.resources.ResourcePath;

import com.mysema.query.sql.dml.SQLDeleteClause;

/**
 * This operation deletes the markers for a single resource in the workspace.
 * It either deletes markers from a specific origin or from all origins.
 */
public final class DeleteSingleResourceMarkersOperation extends SingleResourceOperation {

	/**
	 * the origin
	 */
	private MarkerOrigin origin;
	
	/**
	 * Constructor.
	 * @param path the path of the resource
	 * @param origin the origin, or null to delete markers from all origins
	 */
	public DeleteSingleResourceMarkersOperation(final ResourcePath path, final MarkerOrigin origin) {
		super(path);
		this.origin = origin;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.WorkspaceOperationContext)
	 */
	@Override
	protected void perform(final WorkspaceOperationContext context) {
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.path.eq(getPath().withTrailingSeparator(false).toString()));
		if (origin != null) {
			delete.where(QMarkers.markers.origin.eq(origin.toString()));
		}
		trace("will delete resource markers now", getPath());
		delete.execute();
	}

}
