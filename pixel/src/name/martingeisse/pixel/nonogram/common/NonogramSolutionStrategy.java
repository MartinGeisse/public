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
		NonogramBoard falseBoard = tryBranch(board, x, y, depth, false);
		NonogramBoard trueBoard = tryBranch(board, x, y, depth, true);
		boolean winnerValue;
		if (falseBoard != null) {
			if (trueBoard != null) {
				trueBoard.mergeForkFrom(falseBoard);
				board.mergeResultsFrom(trueBoard);
				return false;
			} else {
				winnerValue = false;
			}
		} else {
			if (trueBoard != null) {
				winnerValue = true;
			} else {
				throw new InconsistencyException();
			}
		}
		System.out.println("found cell by forking: " + x + ", " + y + " (" + depth + ")");
		board.setPixel(x, y, winnerValue);
		while (true) {
			int counter = board.getChangeCounter();
			new NonForkingStrategyBundle().run(board);
			if (board.getChangeCounter() == counter) {
				break;
			}
		}
		return true;
	}

	/**
	 * 
	 */
	private static NonogramBoard tryBranch(NonogramBoard board, int x, int y, int depth, boolean value) {
		board = board.clone();
		board.setPixel(x, y, value);
		try {
			new ForkStrategy(depth - 1).run(board);
			return board;
		} catch (InconsistencyException e) {
			return null;
		}
	}
	
}
