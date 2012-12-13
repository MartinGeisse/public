/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.resources;

import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * Model that dynamically fetches marker lists from the database.
 */
public class MarkerDataListModel extends AbstractReadOnlyModel<List<MarkerData>> {

	/**
	 * the fileId
	 */
	private Long fileId;
	
	/**
	 * the fileName
	 */
	private String fileName;
	
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
		this.meaningFilter = meaningFilter;
		this.limit = limit;
	}
	
	/**
	 * Constructor used to fetch markers for a single file.
	 * @param fileId the database ID of the file
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @param limit the maximum number of markers to fetch
	 */
	public MarkerDataListModel(long fileId, MarkerMeaning[] meaningFilter, long limit) {
		this.fileId = fileId;
		this.meaningFilter = meaningFilter;
		this.limit = limit;
	}
	
	/**
	 * Constructor used to fetch markers for a single file.
	 * @param fileName the file name
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @param limit the maximum number of markers to fetch
	 */
	public MarkerDataListModel(String fileName, MarkerMeaning[] meaningFilter, long limit) {
		this.fileName = fileName;
		this.meaningFilter = meaningFilter;
		this.limit = limit;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public List<MarkerData> getObject() {
		if (fileId != null) {
			return MarkerData.fetchMarkerDataForFile(fileId, meaningFilter, limit);
		} else if (fileName != null) {
			return MarkerData.fetchMarkerDataForFile(fileName, meaningFilter, limit);
		} else {
			return MarkerData.fetchMarkerData(meaningFilter, limit);
		}
	}
	
}
