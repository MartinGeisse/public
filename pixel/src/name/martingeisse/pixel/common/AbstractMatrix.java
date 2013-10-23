/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.common;


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
	
}
