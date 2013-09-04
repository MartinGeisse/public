/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.sprite;

/**
 * Contains the information from looking up a sprite in the registry.
 */
public final class SpriteReference {

	/**
	 * the atlas
	 */
	private final SpriteAtlas atlas;
	
	/**
	 * the x
	 */
	private final int x;
	
	/**
	 * the y
	 */
	private final int y;
	
	/**
	 * the width
	 */
	private final int width;
	
	/**
	 * the height
	 */
	private final int height;

	/**
	 * Constructor.
	 * @param atlas the atlas that contains the sprite
	 * @param x the x position in the atlas
	 * @param y the y position in the atlas
	 * @param width the sprite width
	 * @param height the sprite height
	 */
	public SpriteReference(SpriteAtlas atlas, int x, int y, int width, int height) {
		this.atlas = atlas;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Getter method for the atlas.
	 * @return the atlas
	 */
	public SpriteAtlas getAtlas() {
		return atlas;
	}
	
	/**
	 * Getter method for the x.
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Getter method for the y.
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Getter method for the width.
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Getter method for the height.
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
}
