/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.misc;

import name.martingeisse.pixel.nonogram.NonogramBoard;
import name.martingeisse.pixel.nonogram.common.NonogramSolutionStrategy;
import name.martingeisse.pixel.nonogram.fast.NonogramFastStrategy;

/**
 * A bundle containing all non-forking strategies.
 */
public class NonForkingStrategyBundle extends NonogramSolutionStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.common.NonogramSolutionStrategy#run(name.martingeisse.pixel.nonogram.NonogramBoard)
	 */
	@Override
	public void run(NonogramBoard board) {
		for (int i=0; i<10; i++) {
			new NonogramFastStrategy().run(board);
			new SliceSpanCombinationsStrategy().run(board);
		}
	}
	
}
