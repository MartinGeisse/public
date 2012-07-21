/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.page;

import name.martingeisse.admin.navigation.INavigationLocator;

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
	 * @see name.martingeisse.admin.navigation.INavigationLocator#getCurrentNavigationPath()
	 */
	@Override
	public String getCurrentNavigationPath() {
		return "/";
	}

}
