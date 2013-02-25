/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * Model that dynamically fetches marker lists from the database.
 */
public class MarkerDataListModel extends AbstractReadOnlyModel<List<FetchMarkerResult>> {

	/**
	 * the path
	 */
	private ResourcePath path;
	
	/**
	 * the meaningFilter
	 */
	private MarkerMeaning[] meaningFilter;
	
	/**
	 * the limit
	 */
	private long limit;
	
	/**
	 * Constructor used to fetch markers for all files.
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @param limit the maximum number of markers to fetch
	 */
	public MarkerDataListModel(MarkerMeaning[] meaningFilter, long limit) {
		this.path = null;
		this.meaningFilter = meaningFilter;
		this.limit = limit;
	}
	
	/**
	 * Constructor used to fetch markers for a single resource.
	 * @param path the path of the resource
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @param limit the maximum number of markers to fetch
	 */
	public MarkerDataListModel(ResourcePath path, MarkerMeaning[] meaningFilter, long limit) {
		this.path = path;
		this.meaningFilter = meaningFilter;
		this.limit = limit;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public List<FetchMarkerResult> getObject() {
		if (path != null) {
			return Workspace.fetchSingleResourceMarkers(path, meaningFilter, limit);
		} else {
			return Workspace.fetchAllMarkers(meaningFilter, limit);
		}
	}
	
}
