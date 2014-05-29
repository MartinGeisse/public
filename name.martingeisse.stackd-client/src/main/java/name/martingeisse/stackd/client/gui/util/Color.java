/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.util;

import org.lwjgl.opengl.GL11;

/**
 * An RGBA color. Components are measured in the range 0..255.
 */
public final class Color {

	/**
	 * the TRANSPARENT
	 */
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	
	/**
	 * the BLACK
	 */
	public static final Color BLACK = new Color(0, 0, 0, 255);

	/**
	 * the BLUE
	 */
	public static final Color BLUE = new Color(0, 0, 255, 255);
	
	/**
	 * the GREEN
	 */
	public static final Color GREEN = new Color(0, 255, 0, 255);
	
	/**
	 * the CYAN
	 */
	public static final Color CYAN = new Color(0, 255, 255, 255);
	
	/**
	 * the RED
	 */
	public static final Color RED = new Color(255, 0, 0, 255);
	
	/**
	 * the PURPLE
	 */
	public static final Color PURPLE = new Color(255, 0, 255, 255);
	
	/**
	 * the YELLOW
	 */
	public static final Color YELLOW = new Color(255, 255, 0, 255);
	
	/**
	 * the WHITE
	 */
	public static final Color WHITE = new Color(255, 255, 255, 255);
	
	/**
	 * the red
	 */
	private final int red;
	
	/**
	 * the green
	 */
	private final int green;
	
	/**
	 * the blue
	 */
	private final int blue;
	
	/**
	 * the alpha
	 */
	private final int alpha;

	/**
	 * Constructor.
	 * @param red the red component of the color
	 * @param green the green component of the color
	 * @param blue the blue component of the color
	 * @param alpha the alpha component of the color
	 */
	public Color(int red, int green, int blue, int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	/**
	 * Constructor.
	 * @param red the red component of the color
	 * @param green the green component of the color
	 * @param blue the blue component of the color
	 */
	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = 255;
	}

	/**
	 * Getter method for the red.
	 * @return the red
	 */
	public int getRed() {
		return red;
	}
	
	/**
	 * Getter method for the green.
	 * @return the green
	 */
	public int getGreen() {
		return green;
	}
	
	/**
	 * Getter method for the blue.
	 * @return the blue
	 */
	public int getBlue() {
		return blue;
	}
	
	/**
	 * Getter method for the alpha.
	 * @return the alpha
	 */
	public int getAlpha() {
		return alpha;
	}

	/**
	 * Sets this color as the current drawing color in OpenGL.
	 */
	public void glColor() {
		GL11.glColor4ub((byte)red, (byte)green, (byte)blue, (byte)alpha);
	}
	
	/**
	 * Sets this color as the current drawing color in OpenGL,
	 * applying another external alpha value as well as the alpha value
	 * from this color. The external alpha value too should be in the range 0..255.
	 * 
	 * @param externalAlpha the external alpha value to apply
	 */
	public void glColorWithCombinedAlpha(int externalAlpha) {
		int effectiveAlpha = (alpha * externalAlpha) >> 8;
		GL11.glColor4ub((byte)red, (byte)green, (byte)blue, (byte)effectiveAlpha);
	}
	
}
