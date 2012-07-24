/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.entity.EntityConfigurationUtil;

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
	 * @see name.martingeisse.admin.application.IPlugin#unbox()
	 */
	@Override
	public IPlugin[] unbox() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute()
	 */
	@Override
	public void contribute() {
		EntityConfigurationUtil.addRawEntityListPropertyDisplayFilter(this);
	}
	
}
