/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

import name.martingeisse.admin.navigation.INavigationLocator;
import name.martingeisse.admin.navigation.NavigationTreeSelector;

/**
 * The "home" page.
 */
public class HomePage extends AbstractAdminPage implements INavigationLocator {

	/**
	 * Constructor.
	 */
	public HomePage() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationLocator#getCurrentNavigationPath(name.martingeisse.admin.navigation.NavigationTreeSelector)
	 */
	@Override
	public String getCurrentNavigationPath(NavigationTreeSelector treeSelector) {
		return "/";
	}

}
