/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.schema.reference.FixedNameEntityReferenceDetector;

/**
 * This plugin adds all customization code.
 */
public class CustomizationPlugin implements IPlugin {

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
		EntityConfigurationUtil.addEntityReferenceDetector(new FixedNameEntityReferenceDetector(null, "modificationUser_id", "User"));
	}

}
