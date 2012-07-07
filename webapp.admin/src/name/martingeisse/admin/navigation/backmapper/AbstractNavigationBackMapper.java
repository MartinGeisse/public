/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.backmapper;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationTree;

/**
 * This class provides an empty initialize() method and the ability to add
 * a back-mapper as a plugin that contributes itself.
 */
public abstract class AbstractNavigationBackMapper implements INavigationBackMapper, IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute() {
		NavigationConfigurationUtil.addNavigationBackMapper(this);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.INavigationBackMapper#initialize(name.martingeisse.admin.navigation.NavigationTree)
	 */
	@Override
	public void initialize(NavigationTree tree) {
	}
	
}
