/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Base class for picture and board implementations.
 */
public abstract class AbstractMatrix {

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
	 * @param width the width of the matrix
	 * @param height the height of the matrix
	 */
	public AbstractMatrix(int width, int height) {
		this.width = width;
		this.height = height;
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
	 * Returns the index of a cell in the backing array.
	 */
	protected int getIndex(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			throw new IndexOutOfBoundsException("invalid matrix position: (" + x + ", " + y + ") -- image size: " + width + " x " + height);
		}
		return y * width + x;
	}

	/**
	 * Renders a not-yet-disposed {@link DrawHelper} from this matrix.
	 * @param cellSize the size of each matrix pixel, measured in returned image pixels
	 * @param grid whether to draw a grid
	 * @return the draw helper
	 */
	public abstract DrawHelper renderToDrawHelper(int cellSize, boolean grid);

	/**
	 * Renders a {@link BufferedImage} from this matrix.
	 * @param cellSize the size of each matrix's pixel, measured in returned image pixels
	 * @param grid whether to draw a grid
	 * @return the image
	 */
	public final BufferedImage renderToBufferedImage(int cellSize, boolean grid) {
		DrawHelper helper = renderToDrawHelper(cellSize, grid);
		helper.dispose();
		return helper.getImage();
	}

	/**
	 * Renders a PNG file from this matrix.
	 * @param cellSize the size of each matrix's pixel, measured in returned image pixels
	 * @param grid whether to draw a grid
	 * @param file the file to render to
	 * @throws IOException on I/O errors
	 */
	public final void renderToPngFile(int cellSize, boolean grid, File file) throws IOException {
		DrawHelper helper = renderToDrawHelper(cellSize, grid);
		helper.dispose();
		helper.writePngFile(file);
	}
	
}
