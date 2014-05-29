/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.util;

/**
 * Constants for horizontal alignment.
 */
public enum HorizontalAlignment {

	/**
	 * the LEFT
	 */
	LEFT,
	
	/**
	 * the CENTER
	 */
	CENTER,
	
	/**
	 * the RIGHT
	 */
	RIGHT;
	
	/**
	 * Returns the x coordinate of a point that uses this alignment in
	 * a container of the specified outer width.
	 * 
	 * @param outerWidth the width of the container
	 * @return the x coordinate of the point
	 */
	public final int alignPoint(int outerWidth) {
		if (this == LEFT) {
			return 0;
		} else if (this == CENTER) {
			return outerWidth / 2;
		} else {
			return outerWidth;
		}
	}

	/**
	 * Returns the x coordinate of a span of the specified inner width that
	 * uses this alignment in a container of the specified outer width.
	 * 
	 * @param outerWidth the width of the container
	 * @param innerWidth the width of the span to align
	 * @return the x coordinate of the span
	 */
	public final int alignSpan(int outerWidth, int innerWidth) {
		if (this == LEFT) {
			return 0;
		} else if (this == CENTER) {
			return (outerWidth - innerWidth) / 2;
		} else {
			return (outerWidth - innerWidth);
		}
	}
	
}
