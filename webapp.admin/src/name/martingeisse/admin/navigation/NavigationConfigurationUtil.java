/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import name.martingeisse.admin.application.ApplicationConfiguration;

/**
 * Utilities to access navigation-related configuration in the {@link ApplicationConfiguration}.
 * 
 * TODO: implement a set of global debugging flags to enable
 * debugging like this at runtime and just for some users; disable
 * for others to prevent excessive logging and increase performance
 */
public final class NavigationConfigurationUtil {

	/**
	 * The parameter key for the navigation tree.
	 */
	public static final Class<NavigationTree> NAVIGATION_TREE_PARAMETER_KEY = NavigationTree.class;

	/**
	 * Prevent instantiation.
	 */
	private NavigationConfigurationUtil() {
	}

	/**
	 * Getter method for the navigation tree.
	 * @return the navigation tree
	 */
	public static NavigationTree getNavigationTree() {
		return ApplicationConfiguration.get().getParameters().get(NAVIGATION_TREE_PARAMETER_KEY);
	}

	/**
	 * Setter method for the navigation tree.
	 * @param navigationTree the navigation tree to set
	 */
	public static void setNavigationTree(final NavigationTree navigationTree) {
		ApplicationConfiguration.get().getParameters().set(NAVIGATION_TREE_PARAMETER_KEY, navigationTree);
	}
	
}
