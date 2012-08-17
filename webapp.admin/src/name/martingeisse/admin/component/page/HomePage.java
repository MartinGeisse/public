/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.page;

import name.martingeisse.admin.navigation.INavigationLocationAware;

import org.apache.wicket.markup.html.IHeaderResponse;

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

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.component.page.AbstractAdminPage#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);
	}

}
