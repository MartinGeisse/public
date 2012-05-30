/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import name.martingeisse.admin.application.capabilities.ApplicationCapabilities;
import name.martingeisse.admin.application.capabilities.DefaultEntityReferenceDetector;
import name.martingeisse.admin.application.capabilities.SharedEntityPresentationContributor;
import name.martingeisse.admin.single.RawEntityPresentationPanel;
import name.martingeisse.admin.single.SingleEntityPresenter;

/**
 * This plugin contributes all default behavior.
 */
public class DefaultPlugin implements IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute(ApplicationCapabilities applicationCapabilities) {
		applicationCapabilities.getEntityReferenceDetectors().add(new DefaultEntityReferenceDetector());
		applicationCapabilities.getEntityPresentationContributors().add(new SharedEntityPresentationContributor(null, new SingleEntityPresenter("default", "Default", RawEntityPresentationPanel.class)));
	}

}
