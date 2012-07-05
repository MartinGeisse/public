/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import name.martingeisse.admin.entity.DefaultEntityReferenceDetector;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.SharedEntityPresentationContributor;
import name.martingeisse.admin.entity.single.RawEntityPresentationPanel;
import name.martingeisse.admin.entity.single.SingleEntityPresenter;

/**
 * This plugin contributes all default behavior.
 */
public class DefaultPlugin implements IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute() {
		EntityConfigurationUtil.addEntityReferenceDetector(new DefaultEntityReferenceDetector());
		EntityConfigurationUtil.addEntityPresentationContributor(new SharedEntityPresentationContributor(null, new SingleEntityPresenter("default", "Default", RawEntityPresentationPanel.class)));
	}

}
