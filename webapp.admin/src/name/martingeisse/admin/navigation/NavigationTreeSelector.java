/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * This type selects one of multiple navigation trees.
 */
public enum NavigationTreeSelector {
	
	/**
	 * Selects the global navigation tree.
	 */
	GLOBAL("name.martingeisse.admin.navigation.path.global"),
	
	/**
	 * Selects the local navigation tree within an entity instance. Only
	 * valid within entity instances.
	 */
	ENTITY_INSTANCE("name.martingeisse.admin.navigation.path.entityInstance");

	/**
	 * the pageParameterName
	 */
	private String pageParameterName;

	/**
	 * Constructor.
	 */
	private NavigationTreeSelector(String pageParameterName) {
		this.pageParameterName = pageParameterName;
	}

	/**
	 * Getter method for the pageParameterName.
	 * @return the pageParameterName
	 */
	public String getPageParameterName() {
		return pageParameterName;
	}
	
	/**
	 * Returns the parameter value for the navigation tree selected by this selector.
	 * @param parameters the page parameters to take the value from
	 * @return the parameter value, or null if no such parameter is set
	 */
	public String getParameterValue(PageParameters parameters) {
		if (parameters == null) {
			return null;
		}
		StringValue value = parameters.get(pageParameterName);
		return (value == null ? null : value.toString());
	}
	
	/**
	 * Sets the value of the parameter for the navigation tree selected by this selector
	 * to the specified value, or clears it if the value is null.
	 * @param parameters the parameters collection to set the parameter for
	 * @param value the value, or null to clear
	 */
	public void setParameterValue(PageParameters parameters, String value) {
		if (value == null) {
			parameters.remove(pageParameterName);
		} else {
			parameters.set(pageParameterName, value);
		}
	}
	
	/**
	 * Obtains the navigation path for the specified page. This tries to use the page
	 * as an {@link INavigationLocator} and also attempts to find page parameters
	 * set by a {@link NavigationMountedRequestMapper}.
	 *  
	 * TODO: The page is always the current page, at least as it works now -- is this generally the case?
	 * If so, remove that parameter and make this function access the current
	 * request cycle.
	 * 
	 * @param page the page to return the navigation path for 
	 * @return the navigation path, or null if not found
	 */
	public String getNavigationPath(WebPage page) {
		
		// try INavigationLocator
		if (page instanceof INavigationLocator) {
			INavigationLocator locator = (INavigationLocator)page;
			String result = locator.getCurrentNavigationPath(this);
			if (result != null) {
				return result;
			}
		}
		
		// try page parameters from NavigationMountedRequestMapper.
		// We use the presence of the global path parameter to indicate whether parameters are set.
		PageParameters parameters = page.getPageParameters();
		if (GLOBAL.getParameterValue(parameters) != null) {
			return getParameterValue(parameters);
		}

		// not found
		return null;
		
	}
	
}
