/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities.wicket;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.application.capabilities.ApplicationCapabilities;

/**
 * Base implementation of {@link IWebApplicationInitializationContributor} that
 * behaves as a plugin that contributes itself.
 */
public abstract class AbstractWebApplicationInitializationContributor implements IWebApplicationInitializationContributor, IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute(ApplicationCapabilities applicationCapabilities) {
		applicationCapabilities.getWebApplicationInitializationContributors().add(this);
	}
	
}
