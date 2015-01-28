/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.system;

import java.io.IOException;
import java.io.InputStream;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.Texture;

/**
 * Wraps an image and provides OpenGL texture handling functions.
 */
public final class StackdTexture {

	/**
	 * the KEY_COLOR
	 */
	private static final int[] KEY_COLOR = {255, 0, 255};
	
	/**
	 * the slickTexture
	 */
	private final Texture slickTexture;
	
	/**
	 * Constructor.
	 * @param anchorClass this class specifies the package to load from
	 * @param filename the filename to load from
	 * @param flipped whether to y-flip the texture
	 */
	public StackdTexture(final Class<?> anchorClass, final String filename, boolean flipped) {
		String resourceName = anchorClass.getName() + '/' + filename;
		try (InputStream inputStream = anchorClass.getResourceAsStream(filename)) {
			if (inputStream == null) {
				throw new RuntimeException("file not found: " + filename);
			}
			// TODO slick generates mipmaps *before* it removes the key color pixels, so with GL_LINEAR they bleed into the neighbor pixels
		    this.slickTexture = InternalTextureLoader.get().getTexture(inputStream, resourceName, flipped, GL11.GL_NEAREST, KEY_COLOR);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Calls glBindTexture() on this texture.
	 */
	public void glBindTexture() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, slickTexture.getTextureID());
	}
	
	/**
	 * Getter method for the width.
	 * @return the width
	 */
	public int getWidth() {
		return slickTexture.getImageWidth();
	}
	
	/**
	 * Getter method for the height.
	 * @return the height
	 */
	public int getHeight() {
		return slickTexture.getImageHeight();
	}
	
}
