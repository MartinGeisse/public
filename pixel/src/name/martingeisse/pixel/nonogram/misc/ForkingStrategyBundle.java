/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.misc;

import name.martingeisse.pixel.nonogram.NonogramBoard;
import name.martingeisse.pixel.nonogram.common.NonogramSolutionStrategy;
import name.martingeisse.pixel.nonogram.fast.NonogramFastStrategy;

/**
 * A bundle containing all strategies, including forking.
 */
public class ForkingStrategyBundle extends NonogramSolutionStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.common.NonogramSolutionStrategy#run(name.martingeisse.pixel.nonogram.NonogramBoard)
	 */
	@Override
	public void run(NonogramBoard board) {
		for (int i=0; i<10; i++) {
			for (int j=0; j<500; j++) {
				new NonogramFastStrategy().run(board);
				new SliceSpanCombinationsStrategy().run(board);
			}
			
			System.out.println("fork 1");
			new ForkStrategy(1).run(board);
			
			for (int j=0; j<500; j++) {
				new NonogramFastStrategy().run(board);
				new SliceSpanCombinationsStrategy().run(board);
			}
			
			System.out.println("fork 1");
			new ForkStrategy(1).run(board);
			
			for (int j=0; j<500; j++) {
				new NonogramFastStrategy().run(board);
				new SliceSpanCombinationsStrategy().run(board);
			}

			System.out.println("fork 2");
			new ForkStrategy(2).run(board);
			
		}
	}
	
}
