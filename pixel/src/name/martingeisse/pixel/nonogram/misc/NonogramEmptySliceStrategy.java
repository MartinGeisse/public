/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.misc;

import name.martingeisse.pixel.nonogram.common.NonogramSlicingStrategy;

/**
 * Only marks empty slices. Typically applied to real boards
 * (to be solved by a human) for convenience.
 */
public class NonogramEmptySliceStrategy extends NonogramSlicingStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.NonogramSlicingStrategy#handleSlice(int, int[])
	 */
	@Override
	protected void handleSlice(int primaryLocation, int[] primaryHints) {
		if (primaryHints.length == 0) {
			setPixels(primaryLocation, 0, getSecondarySize(), false);
			return;
		}
	}
	
}
