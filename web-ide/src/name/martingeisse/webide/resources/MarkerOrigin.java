/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

/**
 * This enum defines constants that indicate the origin of a marker.
 * 
 * The origin is typically used to replace all markers from a
 * specific origin by a new set of markers from that origin. For
 * example, the Java compiler defines its own origin and will replace
 * all markers for a file from this origin by a new set of markers
 * when compiling the file.
 */
public enum MarkerOrigin {

	/**
	 * Indicates that the marker originates from the Java compiler.
	 */
	JAVAC,
	
	/**
	 * Indicates that the marker originates from the plugin development environment.
	 */
	PDE;
	
}
