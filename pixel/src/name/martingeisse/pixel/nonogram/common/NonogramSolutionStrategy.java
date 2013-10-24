/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.common;

import name.martingeisse.pixel.common.InconsistencyException;
import name.martingeisse.pixel.nonogram.NonogramBoard;
import name.martingeisse.pixel.nonogram.misc.ForkStrategy;
import name.martingeisse.pixel.nonogram.misc.NonForkingStrategyBundle;

/**
 * Base class to implement solution strategies.
 */
public abstract class NonogramSolutionStrategy {

	/**
	 * Constructor.
	 */
	public NonogramSolutionStrategy() {
	}

	/**
	 * Runs this strategy on the specified board.
	 * @param board the board
	 */
	public abstract void run(NonogramBoard board);

	/**
	 * Tries to fork on the specified pixel.
	 * @param board the board
	 * @param x the x position of the pixel
	 * @param y the y position of the pixel
	 * @param depth the maximum forking depth
	 * @return true on success, false on failure
	 */
	public static boolean forkOn(NonogramBoard board, int x, int y, int depth) {
		boolean falseWorks = tryBranch(board, x, y, depth, false);
		boolean trueWorks = tryBranch(board, x, y, depth, true);
		boolean winnerValue;
		if (falseWorks) {
			if (trueWorks) {
				return false;
			} else {
				winnerValue = false;
			}
		} else {
			if (trueWorks) {
				winnerValue = true;
			} else {
				throw new InconsistencyException();
			}
		}
		System.out.println("+++ " + x + ", " + y + " (" + depth + ")");
		board.setPixel(x, y, winnerValue);
		for (int i = 0; i < 20; i++) {
			new NonForkingStrategyBundle().run(board);
		}
		return true;
	}

	/**
	 * @param board
	 * @param x
	 * @param y
	 * @param depth
	 * @param value
	 * @return
	 */
	public static boolean tryBranch(NonogramBoard board, int x, int y, int depth, boolean value) {
		board = board.clone();
		board.setPixel(x, y, value);
		try {
			new ForkStrategy(depth - 1).run(board);
			return true;
		} catch (InconsistencyException e) {
			return false;
		}
	}
	
}
