/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

/**
 * This type selects one of multiple navigation trees.
 */
public enum NavigationTreeSelector {

	/**
	 * Selects the global navigation tree.
	 */
	GLOBAL,
	
	/**
	 * Selects the local navigation tree within an entity instance. Only
	 * valid within entity instances.
	 */
	ENTITY_INSTANCE;
	
}
