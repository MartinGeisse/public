/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.admin.application.IPlugin;

/**
 * Base implementation of {@link IWebApplicationInitializationContributor} that
 * behaves as a plugin that contributes itself.
 */
public abstract class AbstractWebApplicationInitializationContributor implements IWebApplicationInitializationContributor, IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#unbox()
	 */
	@Override
	public IPlugin[] unbox() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute() {
		WicketConfiguration.webApplicationInitializationCapability.add(this);
	}

}
