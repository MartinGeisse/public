/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram;

/**
 * Implements a few "starter" strategies. These strategies do not take
 * known pixels into account, so they only make sense once at the
 * beginning.
 */
public class NonogramStarterStrategy extends NonogramTransposableStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.NonogramTransposableStrategy#runForTransposition()
	 */
	@Override
	public void runForTransposition() {
		int primarySize = getPrimarySize();
		int secondarySize = getSecondarySize();
		int[][] primaryHints = getPrimaryHints();
		for (int i=0; i<primarySize; i++) {
			if (primaryHints[i].length == 0) {
				setPixels(i, 0, secondarySize, false);
			} else if (primaryHints[i].length == 1) {
				int rangeLength = primaryHints[i][0];
				int missing = secondarySize - rangeLength;
				if (missing < rangeLength) {
					setPixels(i, missing, secondarySize - 2 * missing, true);
				}
			}
		}
	}
	
}
