/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.common;


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
	 * Renders the pixels for this board.
	 * @param helper the draw helper to render to
	 * @param grid whether to draw a grid
	 */
	protected final void renderPixels(DrawHelper helper, boolean grid) {
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
		if (grid) {
			helper.drawGrid(getWidth(), getHeight());
		}
	}
	
}
