/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram;

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
		
		//
		if (primaryHints.length == 0) {
			setPixels(primaryLocation, 0, getSecondarySize(), false);
			return;
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.NonogramTransposableStrategy#runForTransposition()
	 */
	@Override
	public void runForTransposition() {
		int primarySize = getPrimarySize();
		int secondarySize = getSecondarySize();
		int[][] primaryHints = getPrimaryHints();
		for (int i=0; i<primarySize; i++) {
		}
	}

	private void findStarter(int primary, int[] primaryHints) {
		
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
				setPixels(primary, minStart + missing, left, true);
			}
			minStart = minStart + primaryHint + 1;
		}
		
	}
	
}
