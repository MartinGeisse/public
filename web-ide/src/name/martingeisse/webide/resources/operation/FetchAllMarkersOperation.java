/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.util.ArrayUtil;
import name.martingeisse.webide.entity.Markers;
import name.martingeisse.webide.entity.QMarkers;
import name.martingeisse.webide.resources.MarkerData;
import name.martingeisse.webide.resources.MarkerMeaning;

import com.mysema.query.sql.SQLQuery;

/**
 * Fetches the markers for a all resources and makes them
 * available through a getter method.
 */
public final class FetchAllMarkersOperation extends WorkspaceOperation {

	/**
	 * the meaningFilter
	 */
	private final MarkerMeaning[] meaningFilter;

	/**
	 * the limit
	 */
	private final long limit;
	
	/**
	 * the markers
	 */
	private List<MarkerData> markers;
	
	/**
	 * Constructor.
	 * @param meaningFilter the requested marker meanings, or null to request all markers
	 * @param limit the maximum number of markers to fetch, or a negative number for no limit
	 */
	public FetchAllMarkersOperation(final MarkerMeaning[] meaningFilter, long limit) {
		this.meaningFilter = meaningFilter;
		this.limit = limit;
		this.markers = null;
	}
	
	/**
	 * Getter method for the meaningFilter.
	 * @return the meaningFilter
	 */
	public MarkerMeaning[] getMeaningFilter() {
		return meaningFilter;
	}
	
	/**
	 * Getter method for the limit.
	 * @return the limit
	 */
	public long getLimit() {
		return limit;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.IWorkspaceOperationContext)
	 */
	@Override
	protected void perform(IWorkspaceOperationContext context) {
		if (meaningFilter != null && meaningFilter.length == 0) {
			this.markers = new ArrayList<MarkerData>();
			return;
		}
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query = query.from(QMarkers.markers);
		if (meaningFilter != null) {
			query = query.where(QMarkers.markers.meaning.in(ArrayUtil.toStringArray(meaningFilter)));
		}
		query.limit(limit);
		List<Markers> rawMarkers = query.list(QMarkers.markers);
		this.markers = new ArrayList<MarkerData>();
		for (Markers marker : rawMarkers) {
			this.markers.add(new MarkerData(marker));
		}
	}

	/**
	 * Getter method for the markers.
	 * @return the markers
	 */
	public List<MarkerData> getMarkers() {
		return markers;
	}
	
}
