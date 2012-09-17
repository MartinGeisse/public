/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import name.martingeisse.admin.entity.EntityCapabilities;
import name.martingeisse.admin.entity.schema.reference.DefaultEntityReferenceDetector;

/**
 * This plugin contributes all default capabilities.
 */
public class DefaultPlugin implements IPlugin {

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
		EntityCapabilities.entityReferenceDetectorCapability.add(new DefaultEntityReferenceDetector());
	}

}
