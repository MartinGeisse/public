/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.common;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * Represents a fully defined pixel picture. The picture's dimensions
 * are fixed after construction, but the contents can be modified.
 * 
 * Pixel colors are represented by boolean values. "true" means a
 * filled (black) pixel, false an empty (white) pixel.
 */
public final class Picture extends AbstractMatrix {

	/**
	 * the pixels
	 */
	private final boolean[] pixels;

	/**
	 * Constructor.
	 * @param image the image to create an instance from
	 */
	public Picture(BufferedImage image) {
		this(image.getWidth(), image.getHeight());
		
		if (image.getType() != BufferedImage.TYPE_BYTE_GRAY && image.getType() != BufferedImage.TYPE_USHORT_GRAY) {
			throw new IllegalArgumentException("only grayscale images are supported by this constructor");
		}
		Raster raster = image.getRaster();
		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				int sample = raster.getSample(x, y, 0);
				setPixel(x, y, sample < 128);
			}
		}
	}

	/**
	 * Constructor.
	 * @param width the width of the picture
	 * @param height the height of the picture
	 */
	public Picture(int width, int height) {
		super(width, height);
		this.pixels = new boolean[width * height];
	}
	
	/**
	 * Returns the color of a single pixel.
	 * @param x the x position of the pixel
	 * @param y the y position of the pixel
	 * @return the pixel color
	 */
	public boolean getPixel(int x, int y) {
		return pixels[getIndex(x, y)];
	}
	
	/**
	 * Sets the color of a single pixel.
	 * @param x the x position of the pixel
	 * @param y the y position of the pixel
	 * @param filled the pixel color
	 */
	public void setPixel(int x, int y, boolean filled) {
		pixels[getIndex(x, y)] = filled;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.common.AbstractMatrix#renderToDrawHelper(int, boolean)
	 */
	@Override
	public DrawHelper renderToDrawHelper(int cellSize, boolean grid) {
		DrawHelper helper = new DrawHelper(getWidth() * cellSize + (grid ? 1 : 0), getHeight() * cellSize + (grid ? 1 : 0), cellSize);
		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				if (getPixel(x, y)) {
					helper.drawBlackPixel(x, y);
				}
			}
		}
		if (grid) {
			helper.drawGrid(getWidth(), getHeight());
		}
		return helper;
	}
	
}
