/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * Helper methods to deal with the implicit page parameter used
 * for navigation-mounted pages.
 */
public final class NavigationUtil {

	/**
	 * The name of the implicit page parameter.
	 */
	public static final String NAVIGATION_PATH_PAGE_PARAMETER_NAME = "name.martingeisse.admin.navigation.path";

	/**
	 * Prevent instantiation.
	 */
	private NavigationUtil() {
	}

	/**
	 * Returns the value of the implicit navigation path parameter.
	 * 
	 * @param parameters the page parameters to take the value from (may be null)
	 * @return the parameter value, or null if no such parameter is set
	 * or if the parameters argument is null
	 */
	public static String getParameterValue(final PageParameters parameters) {
		if (parameters == null) {
			return null;
		}
		final StringValue value = parameters.get(NAVIGATION_PATH_PAGE_PARAMETER_NAME);
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
	public static void setParameterValue(final PageParameters parameters, final String value) {
		if (parameters == null) {
			throw new IllegalArgumentException("'parameters' argument cannot be null");
		} else if (value == null) {
			parameters.remove(NAVIGATION_PATH_PAGE_PARAMETER_NAME);
		} else {
			parameters.set(NAVIGATION_PATH_PAGE_PARAMETER_NAME, value);
		}
	}

	/**
	 * Obtains the current navigation node for the specified path.
	 * 
	 * @param path the navigation path to obtain the node for (may be null)
	 * @param required whether the node is required
	 * @return the current node, or null if no node was found and required is false
	 * @throws IllegalArgumentException if no node was found and required is true
	 */
	public static NavigationNode getNavigationNodeForPath(final String path, final boolean required) throws IllegalArgumentException {
		final NavigationNode node = (path == null ? null : NavigationConfigurationUtil.getNavigationTree().getNodesByPath().get(path));
		if (node != null) {
			return node;
		} else if (required) {
			throw new IllegalArgumentException("page parameter for the navigation path does not match any known path: " + path);
		} else {
			return null;
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
		return getNavigationNodeForPath(getParameterValue(parameters, required), required);
	}

	/**
	 * Obtains the navigation path for the specified page. This tries to use the page
	 * as an {@link INavigationLocationAware} and also attempts to find page parameters
	 * set by a {@link NavigationMountedRequestMapper}.
	 * 
	 * @param page the page to return the navigation path for 
	 * @return the navigation path, or null if not found
	 */
	public static String getNavigationPathForPage(final Page page) {

		// try INavigationLocator
		if (page instanceof INavigationLocationAware) {
			final INavigationLocationAware aware = (INavigationLocationAware)page;
			final String result = aware.getNavigationPath();
			if (result != null) {
				return result;
			}
		}

		// try the implicit page parameter
		return getParameterValue(page.getPageParameters());

	}

	/**
	 * Returns the current navigation node for the specified component.
	 * @param component the component
	 * @return the navigation node
	 */
	public static NavigationNode getNavigationNodeForComponent(final Component component) {
		return getNavigationNodeForPage(component.getPage());
	}

	/**
	 * Returns the current navigation node for the specified page.
	 * @param page the page
	 * @return the navigation node
	 */
	public static NavigationNode getNavigationNodeForPage(final Page page) {
		final String currentNavigationPath = StringUtils.defaultString(NavigationUtil.getNavigationPathForPage(page));
		return NavigationConfigurationUtil.getNavigationTree().getRoot().findMostSpecificNode(currentNavigationPath);
	}

}
