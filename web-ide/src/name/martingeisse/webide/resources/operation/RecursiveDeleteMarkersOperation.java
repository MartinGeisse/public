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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mysema.query.sql.dml.SQLDeleteClause;

/**
 * Recursively deletes all markers (optionally only markers of a specific origin)
 * from all resources at and below the specified root paths.
 */
public final class RecursiveDeleteMarkersOperation extends RecursiveResourceOperation {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(RecursiveDeleteMarkersOperation.class);
	
	/**
	 * the origin
	 */
	private MarkerOrigin origin;

	/**
	 * Constructor.
	 * @param rootPath the root path
	 * @param origin the origin that specified which markers to delete, or null to
	 * delete all markers
	 */
	public RecursiveDeleteMarkersOperation(ResourcePath rootPath, MarkerOrigin origin) {
		super(rootPath);
		this.origin = origin;
	}

	/**
	 * Constructor.
	 * @param rootPaths the root paths. This array is not copied; the behavior
	 * of this class is unspecified if the array is modified after calling
	 * this constructor.
	 * @param origin the origin that specified which markers to delete, or null to
	 * delete all markers
	 */
	public RecursiveDeleteMarkersOperation(ResourcePath[] rootPaths, MarkerOrigin origin) {
		super(rootPaths);
		this.origin = origin;
	}

	/**
	 * Constructor.
	 * @param rootPaths the root paths
	 * @param origin the origin that specified which markers to delete, or null to
	 * delete all markers
	 */
	public RecursiveDeleteMarkersOperation(Collection<ResourcePath> rootPaths, MarkerOrigin origin) {
		super(rootPaths);
		this.origin = origin;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.RecursiveResourceOperation#onLevelFetched(java.util.List)
	 */
	@Override
	protected void onLevelFetched(List<FetchResourceResult> fetchResults) {
		List<Long> resourceIds = new ArrayList<Long>();
		for (FetchResourceResult fetchResult : fetchResults) {
			resourceIds.add(fetchResult.getId());
		}
		if (logger.isTraceEnabled()) {
			logger.trace("deleting markers for resources with IDs: " + StringUtils.join(resourceIds, ", "));
		}
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.workspaceResourceId.in(resourceIds));
		if (origin != null) {
			delete.where(QMarkers.markers.origin.eq(origin.toString()));
		}
		delete.execute();
	}
	
}
