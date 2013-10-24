/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.misc;

import name.martingeisse.pixel.nonogram.common.NonogramSlicingStrategy;

/**
 * Implements a few "starter" strategies. These strategies do not take
 * known pixels into account, so they only make sense once at the
 * beginning.
 */
public class NonogramStarterStrategy extends NonogramSlicingStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.NonogramSlicingStrategy#handleSlice(int, int[])
	 */
	@Override
	protected void handleSlice(int primaryLocation, int[] primaryHints) {
		
		// handle empty slices
		if (primaryHints.length == 0) {
			setPixels(primaryLocation, 0, getSecondarySize(), false);
			return;
		}
		
		// find the number of defined and missing cells
		int secondarySize = getSecondarySize();
		int definedCount = -1;
		for (int primaryHint : primaryHints) {
			definedCount = definedCount + primaryHint + 1;
		}
		int missing = secondarySize - definedCount;
		
		// find definitely filled cells based on that
		int minStart = 0;
		for (int primaryHint : primaryHints) {
			int left = primaryHint - missing;
			if (left > 0) {
				setPixels(primaryLocation, minStart + missing, left, true);
			}
			minStart = minStart + primaryHint + 1;
		}
		
	}
	
}
