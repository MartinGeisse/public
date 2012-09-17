/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import name.martingeisse.admin.application.CapabilityKey;

/**
 * Wicket-related capability keys.
 */
public final class WicketCapabilities {

	/**
	 * The capability key for web application initialization contributors.
	 */
	public static final CapabilityKey<IWebApplicationInitializationContributor> webApplicationInitializationCapability = new CapabilityKey<IWebApplicationInitializationContributor>();
	
	/**
	 * Prevent instantiation.
	 */
	private WicketCapabilities() {
	}

}
