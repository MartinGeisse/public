/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import name.martingeisse.admin.application.ParameterKey;

/**
 * Navigation-related parameter keys.
 */
public final class NavigationParameters {

	/**
	 * The parameter key for the navigation tree.
	 */
	public static final ParameterKey<NavigationTree> navigationTreeParameter = new ParameterKey<NavigationTree>();

	/**
	 * Prevent instantiation.
	 */
	private NavigationParameters() {
	}

}
