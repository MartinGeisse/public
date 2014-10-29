/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.simulation.resources;

import static org.lwjgl.opengl.GL11.GL_UNPACK_ROW_LENGTH;
import static org.lwjgl.opengl.GL11.GL_UNPACK_SKIP_PIXELS;
import static org.lwjgl.opengl.GL11.GL_UNPACK_SKIP_ROWS;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBitmap;
import static org.lwjgl.opengl.GL11.glDrawPixels;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glPixelZoom;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;

/**
 * An OpenGL-capabable font, using a "character atlas" image.
 * The character's image coordinates are resolved as follows:
 * 
 * - first, the character is mapped to an integer index. This allows to
 *   handle sparse Unicode fonts, non-Unicode fonts using different
 *   codepages, and so on.
 *   
 * - the integer index is used to determine the boundary coordinates
 * 
 * - The character is automatically y-flipped to handle OpenGL's reversed
 *   coordinate system compared to image files.
 * 
 * Drawing characters with this font uses OpenGL's current raster position
 * and assumes that this position has been set before. This allows using the
 * font both for the HUD (using glWindowPos) or "floating" text
 * in the 3d world (using glRasterPos).
 */
public abstract class Font {

	/**
	 * Aligns the top boundary of the text with the current raster position.
	 */
	public static final int ALIGN_TOP = 0;
	
	/**
	 * Aligns the left boundary of the text with the current raster position.
	 */
	public static final int ALIGN_LEFT = 0;
	
	/**
	 * Aligns the horizontal or vertical center of the text with the current raster position.
	 */
	public static final int ALIGN_CENTER = 1;
	
	/**
	 * Aligns the bottom boundary of the text with the current raster position.
	 */
	public static final int ALIGN_BOTTOM = 2;
	
	/**
	 * Aligns the right boundary of the text with the current raster position.
	 */
	public static final int ALIGN_RIGHT = 2;
	
	/**
	 * Draws text using this font.
	 * 
	 * @param s the string of text to draw
	 * @param zoom the zoom factor
	 * @param horizontalAlignment how to align the text horizontally
	 * @param verticalAlignment how to align the text vertically
	 */
	public void drawText(final String s, final float zoom, final int horizontalAlignment, final int verticalAlignment) {
		
		// determine width and height of the string
		int stringWidth = getStringWidth(s), height = getCharacterHeight();
		
		// move the current raster position according to the alignment
		{
			int dx, dy;
			if (horizontalAlignment == ALIGN_CENTER) {
				dx = -stringWidth / 2;
			} else if (horizontalAlignment == ALIGN_RIGHT) {
				dx = -stringWidth;
			} else {
				dx = 0;
			}
			if (verticalAlignment == ALIGN_CENTER) {
				dy = height / 2;
			} else if (verticalAlignment == ALIGN_BOTTOM) {
				dy = height;
			} else {
				dy = 0;
			}
			glBitmap(0, 0, 0, 0, dx * zoom, dy * zoom, null);
		}
		
		// draw the characters
		ByteBuffer image = getImage();
		glPixelStorei(GL_UNPACK_ROW_LENGTH, getImageWidth());
		for (int i=0; i<s.length(); i++) {
			int characterIndex = getCharacterIndex(s.charAt(i));
			if (characterIndex >= 0) {
				int characterWidth = getCharacterWidth(characterIndex);
				glPixelStorei(GL_UNPACK_SKIP_ROWS, getCharacterTopBoundary(characterIndex));
				glPixelStorei(GL_UNPACK_SKIP_PIXELS, getCharacterLeftBoundary(characterIndex));
				glPixelZoom(zoom, -zoom);
				glDrawPixels(characterWidth, height, GL11.GL_ALPHA, GL_UNSIGNED_BYTE, image);
				glBitmap(0, 0, 0, 0, characterWidth * zoom, 0, null);
			}
		}
		glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
		glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
		glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
		
	}

	/**
	 * Returns the font atlas image.
	 * @return the image
	 */
	protected abstract ByteBuffer getImage();
	
	/**
	 * Returns the width of the font atlas image
	 * @return the image width
	 */
	protected abstract int getImageWidth();
	
	/**
	 * Determines the character index for the specified character.
	 * May return -1 to indicate a character that is not supported by this font.
	 * 
	 * @param c the character
	 * @return the index of the character in this font or -1
	 */
	protected abstract int getCharacterIndex(char c);

	/**
	 * Returns the left boundary of the specified character.
	 * @param characterIndex the character index
	 * @return the left boundary
	 */
	protected abstract int getCharacterLeftBoundary(int characterIndex);
	
	/**
	 * Returns the top boundary of the specified character.
	 * @param characterIndex the character index
	 * @return the top boundary
	 */
	protected abstract int getCharacterTopBoundary(int characterIndex);
	
	/**
	 * Returns the width of the specified character.
	 * @param characterIndex the character index
	 * @return the width
	 */
	public abstract int getCharacterWidth(int characterIndex);
	
	/**
	 * Returns the width of the specified string.
	 * @param s the string
	 * @return the width
	 */
	public abstract int getStringWidth(String s);

	/**
	 * Returns the height of all characters.
	 * @return the height
	 */
	public abstract int getCharacterHeight();
	
}
