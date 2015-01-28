/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.util;

/**
 * Constants for vertical alignment.
 */
public enum VerticalAlignment {

	/**
	 * the TOP
	 */
	TOP,
	
	/**
	 * the CENTER
	 */
	CENTER,

	/**
	 * the BOTTOM
	 */
	BOTTOM;
	
	/**
	 * Returns the y coordinate of a point that uses this alignment in
	 * a container of the specified outer height.
	 * 
	 * @param outerHeight the height of the container
	 * @return the y coordinate of the point
	 */
	public final int alignPoint(int outerHeight) {
		if (this == TOP) {
			return 0;
		} else if (this == CENTER) {
			return outerHeight / 2;
		} else {
			return outerHeight;
		}
	}

	/**
	 * Returns the y coordinate of a span of the specified inner height that
	 * uses this alignment in a container of the specified outer height.
	 * 
	 * @param outerHeight the height of the container
	 * @param innerHeight the height of the span to align
	 * @return the y coordinate of the span
	 */
	public final int alignSpan(int outerHeight, int innerHeight) {
		if (this == TOP) {
			return 0;
		} else if (this == CENTER) {
			return (outerHeight - innerHeight) / 2;
		} else {
			return (outerHeight - innerHeight);
		}
	}
	
}
