/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.pages;

import name.martingeisse.admin.navigation.INavigationLocationAware;

/**
 * The "home" page.
 */
public class HomePage extends AbstractAdminPage implements INavigationLocationAware {

	/**
	 * Constructor.
	 */
	public HomePage() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationLocationAware#getNavigationPath()
	 */
	@Override
	public String getNavigationPath() {
		return "/";
	}

}
