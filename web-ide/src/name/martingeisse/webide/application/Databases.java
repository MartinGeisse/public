/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.application;

import name.martingeisse.common.database.IDatabaseDescriptor;

/**
 * Static access to all database descriptors.
 */
public class Databases {

	/**
	 * Prevent instantiation.
	 */
	private Databases() {
	}
	
	/**
	 * the main
	 */
	public static IDatabaseDescriptor main;
	
}
