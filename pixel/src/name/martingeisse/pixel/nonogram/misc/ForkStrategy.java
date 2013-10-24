/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.misc;

import name.martingeisse.pixel.nonogram.NonogramBoard;
import name.martingeisse.pixel.nonogram.common.NonogramSolutionStrategy;

/**
 * Loops through all unknown cells. Forks solution of the problem
 * using that cell, setting the cell if one of the branches runs
 * into inconsistency.
 * 
 * This strategy will not fork on two cells at the same time.
 */
public class ForkStrategy extends NonogramSolutionStrategy {

	/**
	 * the depth
	 */
	private final int depth;

	/**
	 * Constructor.
	 * @param depth the maximum forking depth
	 */
	public ForkStrategy(int depth) {
		this.depth = depth;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.common.NonogramSolutionStrategy#run(name.martingeisse.pixel.nonogram.NonogramBoard)
	 */
	@Override
	public void run(NonogramBoard board) {
		if (depth == 0) {
			new NonForkingStrategyBundle().run(board);
			return;
		}
		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				if (board.getPixel(x, y) == null) {
					forkOn(board, x, y, depth);
				}
			}
		}
	}

}
