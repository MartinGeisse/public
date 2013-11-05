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
	private Boolean[] pixels;
	
	/**
	 * the changeCounter
	 */
	private int changeCounter;

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
		this.changeCounter = 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public AbstractBoard clone() {
		AbstractBoard clone = (AbstractBoard)super.clone();
		clone.pixels = clone.pixels.clone();
		return clone;
	}
	
	/**
	 * Getter method for the changeCounter.
	 * @return the changeCounter
	 */
	public int getChangeCounter() {
		return changeCounter;
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
		Boolean previous = pixels[getIndex(x, y)];
		if (previous != filled) {
			if (previous == null || filled == null || !previous.equals(filled)) {
				changeCounter++;
			}
		}
		pixels[getIndex(x, y)] = filled;
	}

	/**
	 * Renders the pixels for this board.
	 * @param helper the draw helper to render to
	 * @param grid whether to draw a grid
	 */
	protected final void renderPixels(DrawHelper helper, boolean grid) {
		if (grid) {
			helper.drawGrid(getWidth(), getHeight());
		}
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
	}

	/**
	 * Merges cells from the specified board into this one, setting
	 * both conflicting cells as well as those that are null in either
	 * board to null.
	 * 
	 * Used to merge common conclusions in a fork strategy. If either
	 * cell is null (unknown) in this context, then the result is
	 * unknown too, since it is unclear if the two boards are consistent.
	 * 
	 * @param other the board to merge into this one
	 */
	public void mergeForkFrom(AbstractBoard other) {
		if (pixels.length != other.pixels.length) {
			throw new IllegalArgumentException("pixel arrays have different size");
		}
		for (int i=0; i<pixels.length; i++) {
			if (pixels[i] != null) {
				if (!pixels[i].equals(other.pixels[i])) {
					pixels[i] = null;
					changeCounter++;
				}
			}
		}
	}

	/**
	 * Merges cells from the specified board into this one, thowing
	 * an {@link InconsistencyException} if a cell is set to true in
	 * one board and to false in the other. If a cell is null in one
	 * board but not in the other, the non-null value is used.
	 * 
	 * Used to merge newly-gained results into the main board. If either
	 * cell is null (unknown) in this context, then the result is the
	 * other cell since the two boards are assumed to be consistent.
	 * 
	 * @param other the board to merge into this one
	 */
	public void mergeResultsFrom(AbstractBoard other) {
		if (pixels.length != other.pixels.length) {
			throw new IllegalArgumentException("pixel arrays have different size");
		}
		for (int i=0; i<pixels.length; i++) {
			if (other.pixels[i] != null) {
				if (pixels[i] == null) {
					System.out.println("found cell by common conclusion after fork");
					pixels[i] = other.pixels[i];
					changeCounter++;
				} else if (!pixels[i].equals(other.pixels[i])) {
					throw new InconsistencyException();
				}
			}
		}
	}
	
}
