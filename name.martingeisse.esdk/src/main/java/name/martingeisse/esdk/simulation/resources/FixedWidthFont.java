/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.simulation.resources;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

/**
 * Default font implementation, based on the following assumption:
 * - the font atlas texture is stored in a variable in this object
 * - the integer index is simply the unicode character number
 * - characters are expected to be stored in a matrix in the image.
 * - row/column to upper/left boundary mapping uses an offset and stride
 * - all characters use the same fixed size
 *
 * You can subclass this class to override any of these assumptions.
 */
public class FixedWidthFont extends Font {

	/**
	 * the image
	 */
	private final ByteBuffer image;

	/**
	 * the imageWidth
	 */
	private final int imageWidth;

	/**
	 * the charactersPerRow
	 */
	private final int charactersPerRow;

	/**
	 * the charactersTotal
	 */
	private final int charactersTotal;

	/**
	 * the leftBoundaryOffset
	 */
	private final int leftBoundaryOffset;

	/**
	 * the leftBoundaryStride
	 */
	private final int leftBoundaryStride;

	/**
	 * the topBoundaryOffset
	 */
	private final int topBoundaryOffset;

	/**
	 * the topBoundaryStride
	 */
	private final int topBoundaryStride;

	/**
	 * the characterWidth
	 */
	private final int characterWidth;

	/**
	 * the characterHeight
	 */
	private final int characterHeight;

	/**
	 * Constructor.
	 * @param image the image that contains characters
	 * @param characterWidth the width of all characters
	 * @param characterHeight the height of all characters
	 */
	public FixedWidthFont(final BufferedImage image, final int characterWidth, final int characterHeight) {
		this.image = extractPixels(image);
		this.imageWidth = image.getWidth();
		this.charactersPerRow = (imageWidth / characterWidth);
		int numberOfRows = (image.getHeight() / characterHeight);
		this.charactersTotal = (charactersPerRow * numberOfRows);
		this.leftBoundaryOffset = 0;
		this.leftBoundaryStride = characterWidth;
		this.topBoundaryOffset = 0;
		this.topBoundaryStride = characterHeight;
		this.characterWidth = characterWidth;
		this.characterHeight = characterHeight;
	}

	/**
	 * Constructor.
	 * @param image the image that contains characters
	 * @param charactersPerRow the number of characters per matrix row
	 * @param charactersTotal the number of character rows
	 * @param characterWidth the width of all characters
	 * @param characterHeight the height of all characters
	 */
	public FixedWidthFont(final BufferedImage image, final int charactersPerRow, final int charactersTotal, final int characterWidth, final int characterHeight) {
		this(extractPixels(image), image.getWidth(), charactersPerRow, charactersTotal, characterWidth, characterHeight);
	}

	/**
	 * Constructor.
	 * @param image the image that contains characters
	 * @param imageWidth the width of the underlying image in pixels
	 * @param charactersPerRow the number of characters per matrix row
	 * @param charactersTotal the number of character rows
	 * @param characterWidth the width of all characters
	 * @param characterHeight the height of all characters
	 */
	public FixedWidthFont(final ByteBuffer image, final int imageWidth, final int charactersPerRow, final int charactersTotal, final int characterWidth, final int characterHeight) {
		this(image, imageWidth, charactersPerRow, charactersTotal, 0, characterWidth, 0, characterHeight, characterWidth, characterHeight);
	}

	/**
	 * Constructor.
	 * @param image the image that contains characters
	 * @param charactersPerRow the number of characters per matrix row
	 * @param charactersTotal the number of character rows
	 * @param leftBoundaryOffset the offset of the left matrix boundary from the left image boundary in pixels
	 * @param leftBoundaryStride the x distance between the left boundaries of two adjacent characters in the matrix
	 * @param topBoundaryOffset the offset of the top matrix boundary from the top image boundary in pixels
	 * @param topBoundaryStride the y distance between the top boundaries of two adjacent rows in the matrix
	 * @param characterWidth the width of all characters
	 * @param characterHeight the height of all characters
	 */
	public FixedWidthFont(final BufferedImage image, final int charactersPerRow, final int charactersTotal, final int leftBoundaryOffset, final int leftBoundaryStride, final int topBoundaryOffset, final int topBoundaryStride,
		final int characterWidth, final int characterHeight) {
		this(extractPixels(image), image.getWidth(), charactersPerRow, charactersTotal, leftBoundaryOffset, leftBoundaryStride, topBoundaryOffset, topBoundaryStride, characterWidth, characterHeight);
	}

	/**
	 * Constructor.
	 * @param image the image that contains characters
	 * @param imageWidth the width of the underlying image in pixels
	 * @param charactersPerRow the number of characters per matrix row
	 * @param charactersTotal the number of character rows
	 * @param leftBoundaryOffset the offset of the left matrix boundary from the left image boundary in pixels
	 * @param leftBoundaryStride the x distance between the left boundaries of two adjacent characters in the matrix
	 * @param topBoundaryOffset the offset of the top matrix boundary from the top image boundary in pixels
	 * @param topBoundaryStride the y distance between the top boundaries of two adjacent rows in the matrix
	 * @param characterWidth the width of all characters
	 * @param characterHeight the height of all characters
	 */
	public FixedWidthFont(final ByteBuffer image, final int imageWidth, final int charactersPerRow, final int charactersTotal, final int leftBoundaryOffset, final int leftBoundaryStride,
		final int topBoundaryOffset, final int topBoundaryStride, final int characterWidth, final int characterHeight) {
		this.image = image;
		this.imageWidth = imageWidth;
		this.charactersPerRow = charactersPerRow;
		this.charactersTotal = charactersTotal;
		this.leftBoundaryOffset = leftBoundaryOffset;
		this.leftBoundaryStride = leftBoundaryStride;
		this.topBoundaryOffset = topBoundaryOffset;
		this.topBoundaryStride = topBoundaryStride;
		this.characterWidth = characterWidth;
		this.characterHeight = characterHeight;
	}

	/**
	 * 
	 */
	private static ByteBuffer extractPixels(final BufferedImage image) {
		final int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight());
		for (int y = 0; y < image.getHeight(); y++) {
			final int lineBase = y * image.getWidth();
			for (int x = 0; x < image.getWidth(); x++) {
				final int pixel = pixels[lineBase + x];
				final int r = (pixel >> 16) & 0xFF;
				final int g = (pixel >> 8) & 0xFF;
				final int b = (pixel >> 0) & 0xFF;
				final int value = (r + g + b) / 3;
				buffer.put((byte)value);
			}
		}
		buffer.flip();
		return buffer;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.lowlevel.AbstractFont#getImage()
	 */
	@Override
	protected ByteBuffer getImage() {
		return image;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.lowlevel.AbstractFont#getImageWidth()
	 */
	@Override
	protected int getImageWidth() {
		return imageWidth;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.lowlevel.AbstractFont#getCharacterIndex(char)
	 */
	@Override
	protected int getCharacterIndex(final char c) {
		return (c >= charactersTotal ? -1 : c);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.lowlevel.AbstractFont#getCharacterLeftBoundary(int)
	 */
	@Override
	protected int getCharacterLeftBoundary(final int characterIndex) {
		return leftBoundaryOffset + leftBoundaryStride * (characterIndex % charactersPerRow);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.lowlevel.AbstractFont#getCharacterTopBoundary(int)
	 */
	@Override
	protected int getCharacterTopBoundary(final int characterIndex) {
		return topBoundaryOffset + topBoundaryStride * (characterIndex / charactersPerRow);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.lowlevel.AbstractFont#getCharacterWidth(int)
	 */
	@Override
	public int getCharacterWidth(final int characterIndex) {
		return characterWidth;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.system.Font#getStringWidth(java.lang.String)
	 */
	@Override
	public int getStringWidth(String s) {
		int width = 0;
		for (int i=0; i<s.length(); i++) {
			int characterIndex = getCharacterIndex(s.charAt(i));
			if (characterIndex >= 0) {
				width += getCharacterWidth(characterIndex);
			}
		}
		return width;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.lowlevel.AbstractFont#getCharacterHeight()
	 */
	@Override
	public int getCharacterHeight() {
		return characterHeight;
	}

}
