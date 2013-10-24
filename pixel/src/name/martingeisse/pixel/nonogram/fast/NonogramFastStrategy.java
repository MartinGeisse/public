/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.fast;

import name.martingeisse.pixel.nonogram.NonogramBoard;
import name.martingeisse.pixel.nonogram.common.NonogramSolutionStrategy;

/**
 * Implements the "fast" strategy. This strategy makes a few attempts
 * that take almost no time and can be repeated often. They will not find
 * a solution to more complex problems, but they can solve the easy ones
 * without wasting time in a more complex strategy.
 */
public class NonogramFastStrategy extends NonogramSolutionStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.common.NonogramSolutionStrategy#run(name.martingeisse.pixel.nonogram.NonogramBoard)
	 */
	@Override
	public void run(NonogramBoard board) {
		new SinglePinnedSpanStrategy().run(board);
		new FillSmallBorderGapsStrategy().run(board);
		new CombinedExtremeShiftStrategy().run(board);
	}

}
