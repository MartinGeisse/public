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
	 * The parameter key for the global navigation tree.
	 */
	public static final Class<NavigationTree> GLOBAL_NAVIGATION_TREE_PARAMETER_KEY = NavigationTree.class;

	/**
	 * Prevent instantiation.
	 */
	private NavigationConfigurationUtil() {
	}

	/**
	 * Getter method for the global navigation tree.
	 * @return the global navigation tree
	 */
	public static NavigationTree getGlobalNavigationTree() {
		return ApplicationConfiguration.get().getParameters().get(GLOBAL_NAVIGATION_TREE_PARAMETER_KEY);
	}

	/**
	 * Setter method for the global navigation tree.
	 * @param globalNavigationTree the global navigation tree to set
	 */
	public static void setGlobalNavigationTree(final NavigationTree globalNavigationTree) {
		ApplicationConfiguration.get().getParameters().set(GLOBAL_NAVIGATION_TREE_PARAMETER_KEY, globalNavigationTree);
	}
	
}
