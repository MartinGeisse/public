/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.component.pagination;

import java.util.List;

import name.martingeisse.common.util.ParameterUtil;

/**
 * Returned by the backend to indicate the current status of a pagination
 * component.
 */
public final class PaginationResponseData {

	/**
	 * the totalCount
	 */
	private final int totalCount;

	/**
	 * the currentPageItems
	 */
	private final List<Object> currentPageItems;

	/**
	 * Constructor.
	 * @param totalCount the total number of items in the pagination
	 * @param currentPageItems the items for the current page
	 */
	public PaginationResponseData(int totalCount, List<Object> currentPageItems) {
		this.totalCount = totalCount;
		this.currentPageItems = ParameterUtil.ensureNotNull(currentPageItems, "currentPageItems");
	}

	/**
	 * Getter method for the totalCount.
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * Getter method for the currentPageItems.
	 * @return the currentPageItems
	 */
	public List<Object> getCurrentPageItems() {
		return currentPageItems;
	}

}
