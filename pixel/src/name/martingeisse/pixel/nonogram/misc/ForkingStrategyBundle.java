/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.misc;

import name.martingeisse.pixel.nonogram.NonogramBoard;
import name.martingeisse.pixel.nonogram.common.NonogramSolutionStrategy;

/**
 * A bundle containing all strategies, including forking.
 */
public class ForkingStrategyBundle extends NonogramSolutionStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.common.NonogramSolutionStrategy#run(name.martingeisse.pixel.nonogram.NonogramBoard)
	 */
	@Override
	public void run(NonogramBoard board) {
		while (true) {
			int counter = board.getChangeCounter();
			new NonForkingStrategyBundle().run(board);
			new ForkStrategy(1).run(board);
			if (board.getChangeCounter() == counter) {
				break;
			}
		}
	}
	
}
