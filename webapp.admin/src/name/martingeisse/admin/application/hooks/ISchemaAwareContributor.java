/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.hooks;

import name.martingeisse.admin.application.CapabilityKey;

/**
 * Contributors of this type are run after all plugins have made their contributions
 * and after the application schema has been initialized, but before the
 * navigation tree is prepared for use. The main purpose is to allow application
 * code to make contributions (e.g. to the navigation tree) and be able to use
 * entity descriptors from the application schema for this.
 */
public interface ISchemaAwareContributor {

	/**
	 * The capability key for this interface.
	 */
	public static final CapabilityKey<ISchemaAwareContributor> CAPABILITY_KEY = new CapabilityKey<ISchemaAwareContributor>();
	
	/**
	 * Contributes to the application.
	 */
	public void contribute();
	
}
