/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.common;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Random;

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
		
//		if (image.getType() != BufferedImage.TYPE_BYTE_GRAY && image.getType() != BufferedImage.TYPE_USHORT_GRAY) {
//			throw new IllegalArgumentException("only grayscale images are supported by this constructor");
//		}
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

	/**
	 * 
	 */
	private void checkSameSize(Picture other) {
		if (getWidth() != other.getWidth() || getHeight() != other.getHeight()) {
			throw new IllegalArgumentException("both pictures must have the same size");
		}
	}
	
	/**
	 * Inverts this picture.
	 */
	public void invert() {
		for (int i=0; i<pixels.length; i++) {
			pixels[i] = !pixels[i];
		}
	}
	
	/**
	 * Merges another picture into this one. For each pixel, if the
	 * merged pixels have the same value, that value is used. Otherwise,
	 * the supplied valueForMergedPixels is used.
	 * 
	 * @param other the other picture to merge into this one
	 * @param valueForMixedPixels the value to use for mixed pixels
	 */
	public void merge(Picture other, boolean valueForMixedPixels) {
		checkSameSize(other);
		for (int i=0; i<pixels.length; i++) {
			if (pixels[i] != other.pixels[i]) {
				pixels[i] = valueForMixedPixels;
			}
		}
	}
	
	/**
	 * Merges another picture into this one, computing the XOR value
	 * for each pixel.
	 * 
	 * @param other the other picture to merge into this one
	 */
	public void mergeXor(Picture other) {
		checkSameSize(other);
		for (int i=0; i<pixels.length; i++) {
			pixels[i] ^= other.pixels[i];
		}
	}
	
	/**
	 * Scales this picture to a smaller size, combining (factor^2) pixels into one.
	 * 
	 * This method generates a new picture and leaves this one unchanged.
	 * 
	 * @param factor the scaling factor
	 * @param valueForMixedPixels the value to use for mixed pixels
	 * @return the scaled picture
	 */
	public Picture scaleDown(int factor, boolean valueForMixedPixels) {
		if (getWidth() % factor != 0 || getHeight() % factor != 0) {
			throw new IllegalArgumentException("invalid down-scaling factor");
		}
		Picture result = new Picture(getWidth() / factor, getHeight() / factor);
		for (int i=0; i<result.getWidth(); i++) {
			for (int j=0; j<result.getHeight(); j++) {
				result.setPixel(i, j, getRectangleValue(i*factor, j*factor, factor, factor, valueForMixedPixels));
			}
		}
		return result;
	}
	
	private boolean getRectangleValue(int x, int y, int w, int h, boolean valueForMixedPixels) {
		boolean result = getPixel(x, y);
		for (int i=0; i<w; i++) {
			for (int j=0; j<h; j++) {
				if (result != getPixel(x+i, y+j)) {
					return valueForMixedPixels;
				}
			}
		}
		return result;
	}
	
	/**
	 * Scales this picture to a larger size, stretching each pixel to (factor^2) ones.
	 * 
	 * This method generates a new picture and leaves this one unchanged.
	 * 
	 * @param factor the scaling factor
	 * @return the scaled picture
	 */
	public Picture scaleUp(int factor) {
		Picture result = new Picture(getWidth() * factor, getHeight() * factor);
		for (int i=0; i<getWidth(); i++) {
			for (int j=0; j<getHeight(); j++) {
				result.setRectangle(i*factor, j*factor, factor, factor, getPixel(i, j));
			}
		}
		return result;
	}
	
	/**
	 * Scales this picture to (2x2) the size, stretching black pixels and replacing
	 * white ones by a checkerboard pattern.
	 * 
	 * This method generates a new picture and leaves this one unchanged.
	 * 
	 * @return the scaled picture
	 */
	public Picture scaleToBlackAndCheckerboard() {
		Picture result = new Picture(getWidth() * 2, getHeight() * 2);
		for (int i=0; i<getWidth(); i++) {
			for (int j=0; j<getHeight(); j++) {
				if (getPixel(i, j)) {
					result.setRectangle(i*2, j*2, 2, 2, true);
				} else {
					result.setPixel(i*2+0, j*2+0, false);
					result.setPixel(i*2+0, j*2+1, true);
					result.setPixel(i*2+1, j*2+1, false);
					result.setPixel(i*2+1, j*2+0, true);
				}
			}
		}
		return result;
	}
	
	/**
	 * Scales this picture to (2x2) the size, using alternating checkerboard
	 * patterns intead of black and white.
	 * 
	 * This method generates a new picture and leaves this one unchanged.
	 * 
	 * @return the scaled picture
	 */
	public Picture scaleToCheckerboards() {
		Picture result = new Picture(getWidth() * 2, getHeight() * 2);
		for (int i=0; i<getWidth(); i++) {
			for (int j=0; j<getHeight(); j++) {
				boolean p = getPixel(i, j);
				result.setPixel(i*2+0, j*2+0, !p);
				result.setPixel(i*2+0, j*2+1, p);
				result.setPixel(i*2+1, j*2+1, !p);
				result.setPixel(i*2+1, j*2+0, p);
			}
		}
		return result;
	}
	
	private void setRectangle(int x, int y, int w, int h, boolean value) {
		for (int i=0; i<w; i++) {
			for (int j=0; j<h; j++) {
				setPixel(x+i, y+j, value);
			}
		}
	}
	
	/**
	 * Fills this picture with random black and white pixels.
	 * @param seed the random seed
	 */
	public void random(long seed) {
		Random random = new Random(seed);
		for (int i=0; i<pixels.length; i++) {
			pixels[i] = random.nextBoolean();
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.common.AbstractMatrix#renderToDrawHelper(int, boolean)
	 */
	@Override
	public DrawHelper renderToDrawHelper(int cellSize, boolean grid) {
		DrawHelper helper = new DrawHelper(getWidth() * cellSize + (grid ? 1 : 0), getHeight() * cellSize + (grid ? 1 : 0), cellSize);
		if (grid) {
			helper.drawGrid(getWidth(), getHeight());
		}
		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				if (getPixel(x, y)) {
					helper.drawBlackPixel(x, y);
				}
			}
		}
		return helper;
	}
	
}
