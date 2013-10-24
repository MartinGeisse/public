/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.common;

import name.martingeisse.pixel.common.InconsistencyException;
import name.martingeisse.pixel.nonogram.NonogramBoard;

/**
 * Most strategies inherit from this class.
 * It attempts a solution, then transposes the board and tries again.
 * The board itself isn't actually affected, so all access to the
 * board must happen through methods of this class.
 * 
 * When in "normal" (not transposed) state, the primary axis is the
 * y axis (rows), and the secondary axis is the x axis (columns).
 * 
 * Instances of this class mutate while solving the puzzle and
 * thus cannot be used concurrently.
 */
public abstract class NonogramTransposableStrategy extends NonogramSolutionStrategy {

	/**
	 * the board
	 */
	private NonogramBoard board;
	
	/**
	 * the transposed
	 */
	private boolean transposed;

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.NonogramSolutionStrategy#run(name.martingeisse.pixel.nonogram.NonogramBoard)
	 */
	@Override
	public final void run(NonogramBoard board) {
		this.board = board;
		this.transposed = false;
//		 System.out.println("RUNNING " + getClass() + " for rows");
		runForTransposition();
		this.transposed = true;
//		 System.out.println("RUNNING " + getClass() + " for columns");
		 runForTransposition();
	}

	/**
	 * Getter method for the board.
	 * @return the board
	 */
	public NonogramBoard getBoard() {
		return board;
	}

	/**
	 * Getter method for the transposed.
	 * @return the transposed
	 */
	public final boolean isTransposed() {
		return transposed;
	}
	
	/**
	 * Runs the strategy for the current transposition state.
	 */
	public abstract void runForTransposition();
	
	/**
	 * @return the size of the board along the primary axis
	 */
	public final int getPrimarySize() {
		return transposed ? board.getWidth() : board.getHeight();
	}
	
	/**
	 * @return the size of the board along the secondary axis
	 */
	public final int getSecondarySize() {
		return transposed ? board.getHeight() : board.getWidth();
	}
	
	/**
	 * @return the primary hints for the current transposition state
	 */
	public final int[][] getPrimaryHints() {
		return transposed ? board.getColumnHints() : board.getRowHints();
	}

	/**
	 * @return the secondary hints for the current transposition state
	 */
	public final int[][] getSecondaryHints() {
		return transposed ? board.getRowHints() : board.getColumnHints();
	}

	/**
	 * @param primary the location along the primary axis
	 * @param secondary the location along the secondary axis
	 * @return the pixel color
	 */
	public final Boolean getPixel(int primary, int secondary) {
		if (transposed) {
			return board.getPixel(primary, secondary);
		} else {
			return board.getPixel(secondary, primary);
		}
	}

	/**
	 * @param primary the location along the primary axis
	 * @param secondary the location along the secondary axis
	 * @param filled the pixel color
	 */
	public final void setPixel(int primary, int secondary, Boolean filled) {
		int x = transposed ? primary : secondary;
		int y = transposed ? secondary : primary;
		Boolean previousPixel = board.getPixel(x, y);
		if (previousPixel != null && !previousPixel.equals(filled)) {
			throw new InconsistencyException("inconsistency at (" + x + ", " + y + "): previous: " + previousPixel + ", current: " + filled);
		}
		board.setPixel(x, y, filled);
	}

	/**
	 * Like setPixel(), but will leave the current pixel value alone
	 * if the patch value is null, instead of overwriting with null.
	 * 
	 * @param primary the location along the primary axis
	 * @param secondary the location along the secondary axis
	 * @param filled the pixel color
	 */
	public final void patchPixel(int primary, int secondary, Boolean filled) {
		if (filled != null) {
			setPixel(primary, secondary, filled);
		}
	}

	/**
	 * Sets a range of pixels. The range runs along the secondary axis.
	 * @param primary the location along the primary axis
	 * @param secondaryStart the startlocation along the secondary axis
	 * @param count the number of pixels
	 * @param filled the pixel color
	 */
	public final void setPixels(int primary, int secondaryStart, int count, Boolean filled) {
		for (int i=0; i<count; i++) {
			setPixel(primary, secondaryStart + i, filled);
		}
	}
	
	/**
	 * Like patchPixels(), but will leave the current pixel value alone
	 * if the patch value is null, instead of overwriting with null.
	 * 
	 * @param primary the location along the primary axis
	 * @param secondaryStart the startlocation along the secondary axis
	 * @param count the number of pixels
	 * @param filled the pixel color
	 */
	public final void patchPixels(int primary, int secondaryStart, int count, Boolean filled) {
		for (int i=0; i<count; i++) {
			patchPixel(primary, secondaryStart + i, filled);
		}
	}
	
	/**
	 * Gets a primary slice, i.e. a row or column with a single primary
	 * location that expands along the secondary axis.
	 * 
	 * @param primary the primary location
	 * @return the slice
	 */
	public final Boolean[] getPrimarySlice(int primary) {
		return transposed ? board.copyColumn(primary) : board.copyRow(primary);
	}
	
	/**
	 * Gets a secondary slice, i.e. a row or column with a single secondary
	 * location that expands along the primary axis.
	 * 
	 * @param secondary the secondary location
	 * @return the slice
	 */
	public final Boolean[] getSecondarySlice(int secondary) {
		return transposed ? board.copyRow(secondary) : board.copyColumn(secondary);
	}
	
}
