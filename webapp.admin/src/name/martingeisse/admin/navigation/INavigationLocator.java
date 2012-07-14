/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

/**
 * Finds the current location within the navigation tree. This interface is
 * implemented by pages and entities to mark the current location of
 * the user (i.e. of the current page) in the navigation menu.
 */
public interface INavigationLocator {

	/**
	 * Determines the current location in the navigation tree.
	 * @return the current location, or null if this locator doesn't know
	 */
	public String getCurrentNavigationPath();
	
}
