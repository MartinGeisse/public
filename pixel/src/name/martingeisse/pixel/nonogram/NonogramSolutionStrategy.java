/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram;

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
	 * Runs this strategy on the specified solver.
	 * @param solver the solver
	 */
	public abstract void run(NonogramSolver solver);
	
}
