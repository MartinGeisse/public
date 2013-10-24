/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.common;

/**
 * Base class for all strategies that "slice" the board into rows/columns
 * and treat each one separately. Slices are stacked along the primary axis
 * and expand along the secondary axis.
 */
public abstract class NonogramSlicingStrategy extends NonogramTransposableStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.NonogramTransposableStrategy#runForTransposition()
	 */
	@Override
	public final void runForTransposition() {
		int primarySize = getPrimarySize();
		int[][] primaryHints = getPrimaryHints();
		for (int i=0; i<primarySize; i++) {
			handleSlice(i, primaryHints[i]);
		}
	}
	
	/**
	 * Handles a single slice.
	 * 
	 * @param primaryLocation the primary location of the slice
	 * @param primaryHints the primary hints for the slice
	 */
	protected abstract void handleSlice(int primaryLocation, int[] primaryHints);

}
