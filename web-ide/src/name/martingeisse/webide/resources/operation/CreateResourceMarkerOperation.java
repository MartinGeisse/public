/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QMarkers;
import name.martingeisse.webide.resources.MarkerMeaning;
import name.martingeisse.webide.resources.MarkerOrigin;
import name.martingeisse.webide.resources.ResourcePath;

import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * This operation creates a marker for a resource.
 */
public final class CreateResourceMarkerOperation extends SingleResourceOperation {

	/**
	 * the origin
	 */
	private final MarkerOrigin origin;

	/**
	 * the meaning
	 */
	private MarkerMeaning meaning;

	/**
	 * the line
	 */
	private long line;

	/**
	 * the column
	 */
	private long column;

	/**
	 * the message
	 */
	private String message;

	/**
	 * Constructor.
	 * @param path the path of the resource
	 * @param origin the origin of the marker
	 * @param meaning the meaning of the marker
	 * @param line the line of the marker's position
	 * @param column the column of the marker's position
	 * @param message the marker message
	 */
	public CreateResourceMarkerOperation(final ResourcePath path, final MarkerOrigin origin, final MarkerMeaning meaning, final long line, final long column, final String message) {
		super(path);
		this.origin = origin;
		this.meaning = meaning;
		this.line = line;
		this.column = column;
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.WorkspaceOperationContext)
	 */
	@Override
	protected void perform(final WorkspaceOperationContext context) {
		long resourceId = fetchResourceId(context);
		if (resourceId == -1) {
			throw new WorkspaceResourceNotFoundException(getPath());
		}
		trace("will create resource marker now", getPath());
		SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QMarkers.markers);
		insert.set(QMarkers.markers.workspaceResourceId, resourceId);
		insert.set(QMarkers.markers.origin, origin.toString());
		insert.set(QMarkers.markers.meaning, meaning.toString());
		insert.set(QMarkers.markers.line, line);
		insert.set(QMarkers.markers.column, column);
		insert.set(QMarkers.markers.message, message);
		insert.execute();
	}

}
