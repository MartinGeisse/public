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
		while (true) {
			int outerCounter = board.getChangeCounter();
			
			// "fast" strategy as long as it works
			while (true) {
				int innerCounter = board.getChangeCounter();
				System.out.println("counter: " + board.getChangeCounter() + "   -> trying fast strategy");
				new NonogramFastStrategy().run(board);
				if (board.getChangeCounter() == innerCounter) {
					break;
				}
			}
			
			// slice-span combinations only once, then try "fast" again
			System.out.println("counter: " + board.getChangeCounter() + "   -> trying slice-span combinations strategy");
			new SliceSpanCombinationsStrategy() {
				@Override
				protected void handleSlice(int primaryLocation, int[] primaryHints) {
					NonogramBoard board = getBoard();
					int counter = board.getChangeCounter();
					super.handleSlice(primaryLocation, primaryHints);
					if (board.getChangeCounter() != counter) {
						System.out.println("counter: " + board.getChangeCounter() + ". found something for slice-cell combinations, so trying fast strategy again...");
						while (counter != board.getChangeCounter()) {
							counter = board.getChangeCounter();
							new NonogramFastStrategy().run(board);
						}
						System.out.println("counter: " + board.getChangeCounter() + ". fast strategy interrupt completed");
					}
				}
			}.run(board);
			
			// check condition for the outer loop
			if (board.getChangeCounter() == outerCounter) {
				System.out.println("counter: " + board.getChangeCounter() + " -- non-forking bundle completed");
				break;
			} else {
				System.out.println("counter: " + board.getChangeCounter() + " -- continuing non-forking bundle");
			}
			
		}
	}
	
}
