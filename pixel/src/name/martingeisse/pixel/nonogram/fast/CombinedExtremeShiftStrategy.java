/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.pixel.nonogram.fast;

import name.martingeisse.pixel.nonogram.common.NonogramSlicingStrategy;

/**
 * Shifts all spans to the extremes and finds cells that are
 * covered by the same span in both cases. This is similar to
 * one of the starters except that it takes known empty border
 * cells into account.
 */
public class CombinedExtremeShiftStrategy extends NonogramSlicingStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.common.NonogramSlicingStrategy#handleSlice(int, int[])
	 */
	@Override
	protected void handleSlice(int primaryLocation, int[] primaryHints) {

		// skip empty slices
		if (primaryHints.length == 0) {
			return;
		}
		
		// check for empty border cells
		int sliceStart = 0;
		while (Boolean.FALSE.equals(getPixel(primaryLocation, sliceStart))) {
			sliceStart++;
		}
		int sliceEnd = getSecondarySize();
		while (Boolean.FALSE.equals(getPixel(primaryLocation, sliceEnd - 1))) {
			sliceEnd--;
		}
		int sliceLength = sliceEnd - sliceStart;
		
		// find the number of defined and missing cells
		int definedCount = -1;
		for (int primaryHint : primaryHints) {
			definedCount = definedCount + primaryHint + 1;
		}
		int missing = sliceLength - definedCount;
		
		// find definitely filled cells based on that
		int minStart = 0;
		for (int primaryHint : primaryHints) {
			int left = primaryHint - missing;
			if (left > 0) {
				setPixels(primaryLocation, sliceStart + minStart + missing, left, true);
			}
			minStart = minStart + primaryHint + 1;
		}

	}

}
