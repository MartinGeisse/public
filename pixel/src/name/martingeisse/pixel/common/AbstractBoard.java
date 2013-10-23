/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Base class for game boards. This class manages a matrix of
 * pixels that can be "indeterminate" (represented by null and
 * rendered fully white), "white" (represented by false and
 * rendered white with a dot), or "black" (represented by true and
 * rendered fully black).
 */
public abstract class AbstractBoard extends AbstractMatrix {

	/**
	 * the pixels
	 */
	private final Boolean[] pixels;

	/**
	 * Constructor.
	 * @param picture the picture to create an instance from
	 * @param solved true to initialize the board pixels with the
	 * picture's pixels, false to leave the board clean
	 */
	public AbstractBoard(Picture picture, boolean solved) {
		super(picture.getWidth(), picture.getHeight());
		this.pixels = new Boolean[getWidth() * getHeight()];
		if (solved) {
			for (int x=0; x<getWidth(); x++) {
				for (int y=0; y<getHeight(); y++) {
					setPixel(x, y, picture.getPixel(x, y));
				}
			}
		}
	}

	/**
	 * Returns the color of a single pixel.
	 * @param x the x position of the pixel
	 * @param y the y position of the pixel
	 * @return the pixel color
	 */
	public Boolean getPixel(int x, int y) {
		return pixels[getIndex(x, y)];
	}
	
	/**
	 * Sets the color of a single pixel.
	 * @param x the x position of the pixel
	 * @param y the y position of the pixel
	 * @param filled the pixel color
	 */
	public void setPixel(int x, int y, Boolean filled) {
		pixels[getIndex(x, y)] = filled;
	}

	/**
	 * Renders a not-yet-disposed {@link DrawHelper} from this picture.
	 * @param cellSize the size of each picture's pixel, measured in returned image pixels
	 * @return the draw helper
	 */
	public DrawHelper renderToDrawHelper(int cellSize) {
		DrawHelper helper = new DrawHelper(getWidth() * cellSize + 1, getHeight() * cellSize + 1, cellSize);
		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				Boolean pixel = getPixel(x, y);
				if (pixel == null) {
					// leave white
				} else if (pixel) {
					helper.drawBlackPixel(x, y);
				} else {
					helper.drawLeftWhiteMark(x, y);
				}
			}
		}
		helper.drawGrid(getWidth(), getHeight());
		return helper;
	}

	/**
	 * Renders a {@link BufferedImage} from this picture.
	 * @param cellSize the size of each picture's pixel, meaured in returned image pixels
	 * @return the image
	 */
	public BufferedImage renderToBufferedImage(int cellSize) {
		DrawHelper helper = renderToDrawHelper(cellSize);
		helper.dispose();
		return helper.getImage();
	}

	/**
	 * Renders a PNG file from this picture.
	 * @param cellSize the size of each picture's pixel, meaured in returned image pixels
	 * @param file the file to render to
	 * @throws IOException on I/O errors
	 */
	public void renderToPngFile(int cellSize, File file) throws IOException {
		DrawHelper helper = renderToDrawHelper(cellSize);
		helper.dispose();
		helper.writePngFile(file);
	}
	
}
