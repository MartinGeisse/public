/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.application.capabilities.ApplicationCapabilities;
import name.martingeisse.admin.application.capabilities.INavigationBackMapper;

/**
 * This class provides an empty initialize() method and the ability to add
 * a back-mapper as a plugin that contributes itself.
 */
public abstract class AbstractNavigationBackMapper implements INavigationBackMapper, IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute(ApplicationCapabilities applicationCapabilities) {
		applicationCapabilities.getNavigationBackMappers().add(this);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.INavigationBackMapper#initialize(name.martingeisse.admin.navigation.NavigationTree)
	 */
	@Override
	public void initialize(NavigationTree tree) {
	}
	
}
