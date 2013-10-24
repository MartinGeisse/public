/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.fast;

import name.martingeisse.pixel.nonogram.common.NonogramSlicingStrategy;

/**
	 * Marks border cells "empty" in a slice with a single span that is
	 * already "pinned" by known filled cells.
 */
public class SinglePinnedSpanStrategy extends NonogramSlicingStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.NonogramSlicingStrategy#handleSlice(int, int[])
	 */
	@Override
	protected void handleSlice(int primaryLocation, int[] primaryHints) {

		// prepare
		if (primaryHints.length != 1) {
			return;
		}
		Boolean[] slice = getPrimarySlice(primaryLocation);
		int spanLength = primaryHints[0];
		
		// find first and last pinning cells
		boolean minFilledFound = false;
		int minFilled = 0, maxFilled = 0;
		for (int i=0; i<slice.length; i++) {
			if (slice[i] != null && slice[i]) {
				if (!minFilledFound) {
					minFilledFound = true;
					minFilled = i;
				}
				maxFilled = i;
			}
		}
		if (!minFilledFound) {
			return;
		}
		
		// mark cells before the first pinning cell empty, taking the span length into account
		int minPossible = minFilled - spanLength + 1;
		for (int i=0; i<minPossible; i++) {
			setPixel(primaryLocation, i, false);
		}

		// mark cells after the last pinning cell empty, taking the span length into account
		int minEnd = maxFilled + spanLength;
		for (int i=minEnd; i<slice.length; i++) {
			setPixel(primaryLocation, i, false);
		}

		
	}

}
