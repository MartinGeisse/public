/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.model;

import name.martingeisse.webide.features.simvm.model.ModelElementPlaceholder;

/**
 * Specialized {@link ModelElementPlaceholder} that implements
 * {@link IEcosimModelElement}.
 */
public class EcosimModelElementPlaceholder extends ModelElementPlaceholder implements IEcosimModelElement {

	/**
	 * Constructor.
	 * @param title the title for the UI
	 * @param bodyText the body text for the UI
	 */
	public EcosimModelElementPlaceholder(final String title, final String bodyText) {
		super(title, bodyText);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.ecosim.model.IEcosimModelElement#getContributedDevices()
	 */
	@Override
	public EcosimContributedDevice[] getContributedDevices() {
		return null;
	}

}
