/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

/**
 * This enum defines constants for the meaning of markers. The meaning
 * determines the reason to generate markers and how they are used.
 */
public enum MarkerMeaning {

	/**
	 * used for markers that indicate a user error
	 */
	ERROR,
	
	/**
	 * used for markers that indicate potential problems
	 */
	WARNING;
	
}
