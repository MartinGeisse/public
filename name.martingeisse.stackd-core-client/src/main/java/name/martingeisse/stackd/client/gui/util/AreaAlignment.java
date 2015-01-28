/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.util;

/**
 * Constants for alignment in a 2D area.
 */
public enum AreaAlignment {

	/**
	 * the TOP_LEFT
	 */
	TOP_LEFT(HorizontalAlignment.LEFT, VerticalAlignment.TOP),
	
	/**
	 * the TOP_CENTER
	 */
	TOP_CENTER(HorizontalAlignment.CENTER, VerticalAlignment.TOP),
	
	/**
	 * the TOP_RIGHT
	 */
	TOP_RIGHT(HorizontalAlignment.RIGHT, VerticalAlignment.TOP),
	
	/**
	 * the LEFT_CENTER
	 */
	LEFT_CENTER(HorizontalAlignment.LEFT, VerticalAlignment.CENTER),
	
	/**
	 * the CENTER
	 */
	CENTER(HorizontalAlignment.CENTER, VerticalAlignment.CENTER),
	
	/**
	 * the RIGHT_CENTER
	 */
	RIGHT_CENTER(HorizontalAlignment.RIGHT, VerticalAlignment.CENTER),
	
	/**
	 * the BOTTOM_LEFT
	 */
	BOTTOM_LEFT(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM),
	
	/**
	 * the BOTTOM_CENTER
	 */
	BOTTOM_CENTER(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM),
	
	/**
	 * the BOTTOM_RIGHT
	 */
	BOTTOM_RIGHT(HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
	
	/**
	 * the horizontalAlignment
	 */
	private final HorizontalAlignment horizontalAlignment;

	/**
	 * the verticalAlignment
	 */
	private final VerticalAlignment verticalAlignment;

	/**
	 * Constructor.
	 * @param horizontalAlignment the corresponding horizontal alignment
	 * @param verticalAlignment the corresponding vertical alignment
	 */
	private AreaAlignment(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;
	}

	/**
	 * Getter method for the horizontalAlignment.
	 * @return the horizontalAlignment
	 */
	public final HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}
	
	/**
	 * Getter method for the verticalAlignment.
	 * @return the verticalAlignment
	 */
	public final VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}
	
}
