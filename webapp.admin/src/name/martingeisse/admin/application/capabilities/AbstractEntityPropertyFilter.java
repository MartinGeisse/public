/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import name.martingeisse.admin.application.IPlugin;

/**
 * Base implementation for {@link IRawEntityListPropertyDisplayFilter}. This
 * implementation acts as a plugin to add itself to the application
 * capabilities.
 */
public abstract class AbstractEntityPropertyFilter implements IRawEntityListPropertyDisplayFilter, IPlugin {

	/**
	 * Constructor.
	 */
	public AbstractEntityPropertyFilter() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute(final ApplicationCapabilities applicationCapabilities) {
		applicationCapabilities.getRawEntityListPropertyDisplayFilters().add(this);
	}
	
}
