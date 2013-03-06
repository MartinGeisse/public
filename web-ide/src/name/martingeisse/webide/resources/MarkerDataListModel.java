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
	 * the resourceHandle
	 */
	private ResourceHandle resourceHandle;
	
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
	 * 
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @param limit the maximum number of markers to fetch
	 */
	public MarkerDataListModel(MarkerMeaning[] meaningFilter, long limit) {
		this.resourceHandle = null;
		this.meaningFilter = meaningFilter;
		this.limit = limit;
	}
	
	/**
	 * Constructor used to fetch markers for a single resource.
	 * 
	 * @param resourceHandle the handle for the resource
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @param limit the maximum number of markers to fetch
	 */
	public MarkerDataListModel(ResourceHandle resourceHandle, MarkerMeaning[] meaningFilter, long limit) {
		this.resourceHandle = resourceHandle;
		this.meaningFilter = meaningFilter;
		this.limit = limit;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public List<FetchMarkerResult> getObject() {
		if (resourceHandle != null) {
			return resourceHandle.fetchMarkers(meaningFilter, limit);
		} else {
			return ResourceHandle.fetchAllMarkers(meaningFilter, limit);
		}
	}
	
}
