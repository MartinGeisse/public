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
 * Helper methods to deal with the implicit page parameter used
 * for navigation-mounted pages.
 */
public class NavigationPageParameterUtil {
	
	/**
	 * The name of the implicit page parameter.
	 */
	public static final String NAVIGATION_PATH_PAGE_PARAMETER_NAME = "name.martingeisse.admin.navigation.path";

	/**
	 * Prevent instantiation.
	 */
	private NavigationPageParameterUtil() {
	}
	
	/**
	 * Returns the value of the implicit navigation path parameter.
	 * 
	 * @param parameters the page parameters to take the value from (may be null)
	 * @return the parameter value, or null if no such parameter is set
	 * or if the parameters argument is null
	 */
	public static String getParameterValue(PageParameters parameters) {
		if (parameters == null) {
			return null;
		}
		StringValue value = parameters.get(NAVIGATION_PATH_PAGE_PARAMETER_NAME);
		return (value == null ? null : value.toString());
	}
	
	/**
	 * Returns the value of the implicit navigation path parameter,
	 * optionally enforcing that the parameter is present.
	 * 
	 * @param parameters the page parameters to take the value from (may be null)
	 * @param required whether the path is required
	 * @return the parameter value, or null if no such parameter is set
	 * or if the parameters argument is null (only possible if the required flag
	 * is false).
	 * @throws IllegalArgumentException if no path was found and required is true
	 */
	public static String getParameterValue(final PageParameters parameters, final boolean required) throws IllegalArgumentException {
		final String navigationPath = getParameterValue(parameters);
		if (navigationPath != null) {
			return navigationPath;
		} else if (required) {
			throw new IllegalArgumentException("page parameter for the navigation path is missing from parameters: " + parameters);
		} else {
			return null;
		}
	}
	
	/**
	 * Sets the value of the implicit navigation path parameter to the specified
	 * value, or clears it if the value is null.
	 * 
	 * @param parameters the parameters collection to set the parameter for
	 * @param value the value, or null to clear
	 */
	public static void setParameterValue(PageParameters parameters, String value) {
		if (parameters == null) {
			throw new IllegalArgumentException("'parameters' argument cannot be null");
		} else if (value == null) {
			parameters.remove(NAVIGATION_PATH_PAGE_PARAMETER_NAME);
		} else {
			parameters.set(NAVIGATION_PATH_PAGE_PARAMETER_NAME, value);
		}
	}

	/**
	 * Obtains the current navigation node from the page parameters.
	 * 
	 * @param parameters the page parameters (may be null)
	 * @param required whether the parameter for the current node is required
	 * @return the current node, or null if no node was found and required is false
	 * @throws IllegalArgumentException if no node was found and required is true
	 */
	public static NavigationNode getNavigationNodeFromParameter(final PageParameters parameters, final boolean required) throws IllegalArgumentException {
		final String navigationPath = getParameterValue(parameters, required);
		final NavigationNode node = (navigationPath == null ? null : NavigationConfigurationUtil.getNavigationTree().getNodesByPath().get(navigationPath));
		if (required && node == null) {
			throw new IllegalArgumentException("page parameter for the navigation path does not match any known path: " + navigationPath + ", parameters: " + parameters);
		}
		return node;
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
	public static String getNavigationPathForPage(WebPage page) {
		
		// try INavigationLocator
		if (page instanceof INavigationLocator) {
			INavigationLocator locator = (INavigationLocator)page;
			String result = locator.getCurrentNavigationPath();
			if (result != null) {
				return result;
			}
		}
		
		// try the implicit page parameter
		return getParameterValue(page.getPageParameters());
		
	}
	
}
