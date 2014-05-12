/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A single texture layer. This is a pixel array, with RGBA
 * values per pixel. Each pixel is stored as an (unsigned)
 * byte.
 * 
 * Note that Java has only a signed byte type, so take care
 * when dealing with pixels; operations on them must be made
 * unsigned manually.
 * 
 * Each layer also has (x, y) shift values that map the (0, 0)
 * origin to position (x, y) in the whole image. This allows
 * to move layers relative to each other without taking up
 * pixel memory.
 * 
 * Pixels are stored as an array or rows, each row containing
 * an array of pixels, and each pixel containing R, G, B, A
 * in this order.
 */
public final class Layer implements Cloneable {

	/**
	 * the width
	 */
	private final int width;
	
	/**
	 * the height
	 */
	private final int height;
	
	/**
	 * the pixels
	 */
	private final byte[] pixels;

	/**
	 * the shiftX
	 */
	private int shiftX;
	
	/**
	 * the shiftY
	 */
	private int shiftY;

	/**
	 * Constructor.
	 * @param width the width of this layer in pixels
	 * @param height the height of this layer in pixels
	 */
	public Layer(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new byte[width * height * 4];
		this.shiftX = 0;
		this.shiftY = 0;
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				pixels[(y * width + x) * 4 + 3] = (byte)255;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Layer clone() {
		try {
			Layer result = (Layer)super.clone();
			System.arraycopy(pixels, 0, result.pixels, 0, pixels.length);
			return result;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
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
	
	/**
	 * Getter method for the pixels.
	 * @return the pixels
	 */
	public byte[] getPixels() {
		return pixels;
	}
	
	/**
	 * Getter method for the shiftX.
	 * @return the shiftX
	 */
	public int getShiftX() {
		return shiftX;
	}
	
	/**
	 * Getter method for the shiftY.
	 * @return the shiftY
	 */
	public int getShiftY() {
		return shiftY;
	}
	
	/**
	 * Setter method for the shiftX.
	 * @param shiftX the shiftX to set
	 */
	public void setShiftX(int shiftX) {
		this.shiftX = shiftX;
	}
	
	/**
	 * Setter method for the shiftY.
	 * @param shiftY the shiftY to set
	 */
	public void setShiftY(int shiftY) {
		this.shiftY = shiftY;
	}
	
	/**
	 * Exports this layer as a PNG image to the specified file.
	 * @param file the file to write to
	 * @throws IOException on I/O errors
	 */
	public void exportPng(File file) throws IOException {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		WritableRaster raster = bufferedImage.getRaster();
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				int baseIndex = (y * width + x) * 4;
				for (int band=0; band<4; band++) {
					raster.setSample(x, y, band, pixels[baseIndex + band] & 0xff);
				}
			}
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ImageIO.write(bufferedImage, "png", fileOutputStream);
		fileOutputStream.close();
	}
	
}
