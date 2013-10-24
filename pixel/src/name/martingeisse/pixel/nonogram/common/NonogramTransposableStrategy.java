/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.common;

import name.martingeisse.pixel.nonogram.NonogramSolver;

/**
 * Most strategies inherit from this class.
 * It attempts a solution, then transposes the board and tries again.
 * The board itself isn't actually affected, so all access to the
 * board must happen through methods of this class.
 * 
 * When in "normal" (not transposed) state, the primary axis is the
 * y axis (rows), and the secondary axis is the x axis (columns).
 * 
 * Instances of this class mutate while solving the puzzle and
 * thus cannot be used concurrently.
 */
public abstract class NonogramTransposableStrategy extends NonogramSolutionStrategy {

	/**
	 * the solver
	 */
	private NonogramSolver solver;
	
	/**
	 * the transposed
	 */
	private boolean transposed;

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.NonogramSolutionStrategy#run(name.martingeisse.pixel.nonogram.NonogramSolver)
	 */
	@Override
	public final void run(NonogramSolver solver) {
		this.solver = solver;
		this.transposed = false;
		System.out.println("RUNNING " + getClass() + " for rows");
		runForTransposition();
		this.transposed = true;
		System.out.println("RUNNING " + getClass() + " for columns");
		runForTransposition();
	}

	/**
	 * Getter method for the solver.
	 * @return the solver
	 */
	public final NonogramSolver getSolver() {
		return solver;
	}

	/**
	 * Getter method for the transposed.
	 * @return the transposed
	 */
	public final boolean isTransposed() {
		return transposed;
	}
	
	/**
	 * Runs the strategy for the current transposition state.
	 */
	public abstract void runForTransposition();
	
	/**
	 * @return the size of the board along the primary axis
	 */
	public final int getPrimarySize() {
		return transposed ? solver.getBoard().getWidth() : solver.getBoard().getHeight();
	}
	
	/**
	 * @return the size of the board along the secondary axis
	 */
	public final int getSecondarySize() {
		return transposed ? solver.getBoard().getHeight() : solver.getBoard().getWidth();
	}
	
	/**
	 * @return the primary hints for the current transposition state
	 */
	public final int[][] getPrimaryHints() {
		return transposed ? solver.getBoard().getColumnHints() : solver.getBoard().getRowHints();
	}

	/**
	 * @return the secondary hints for the current transposition state
	 */
	public final int[][] getSecondaryHints() {
		return transposed ? solver.getBoard().getRowHints() : solver.getBoard().getColumnHints();
	}

	/**
	 * @param primary the location along the primary axis
	 * @param secondary the location along the secondary axis
	 * @return the pixel color
	 */
	public final Boolean getPixel(int primary, int secondary) {
		if (transposed) {
			return solver.getBoard().getPixel(primary, secondary);
		} else {
			return solver.getBoard().getPixel(secondary, primary);
		}
	}

	/**
	 * @param primary the location along the primary axis
	 * @param secondary the location along the secondary axis
	 * @param filled the pixel color
	 */
	public final void setPixel(int primary, int secondary, Boolean filled) {
		if (transposed) {
			solver.getBoard().setPixel(primary, secondary, filled);
		} else {
			solver.getBoard().setPixel(secondary, primary, filled);
		}
	}

	/**
	 * Like setPixel(), but will leave the current pixel value alone
	 * if the patch value is null, instead of overwriting with null.
	 * 
	 * @param primary the location along the primary axis
	 * @param secondary the location along the secondary axis
	 * @param filled the pixel color
	 */
	public final void patchPixel(int primary, int secondary, Boolean filled) {
		if (filled != null) {
			setPixel(primary, secondary, filled);
		}
	}

	/**
	 * Sets a range of pixels. The range runs along the secondary axis.
	 * @param primary the location along the primary axis
	 * @param secondaryStart the startlocation along the secondary axis
	 * @param count the number of pixels
	 * @param filled the pixel color
	 */
	public final void setPixels(int primary, int secondaryStart, int count, Boolean filled) {
		for (int i=0; i<count; i++) {
			setPixel(primary, secondaryStart + i, filled);
		}
	}
	
	/**
	 * Like patchPixels(), but will leave the current pixel value alone
	 * if the patch value is null, instead of overwriting with null.
	 * 
	 * @param primary the location along the primary axis
	 * @param secondaryStart the startlocation along the secondary axis
	 * @param count the number of pixels
	 * @param filled the pixel color
	 */
	public final void patchPixels(int primary, int secondaryStart, int count, Boolean filled) {
		for (int i=0; i<count; i++) {
			patchPixel(primary, secondaryStart + i, filled);
		}
	}
	
	/**
	 * Gets a primary slice, i.e. a row or column with a single primary
	 * location that expands along the secondary axis.
	 * 
	 * @param primary the primary location
	 * @return the slice
	 */
	public final Boolean[] getPrimarySlice(int primary) {
		return transposed ? solver.getBoard().copyColumn(primary) : solver.getBoard().copyRow(primary);
	}
	
	/**
	 * Gets a secondary slice, i.e. a row or column with a single secondary
	 * location that expands along the primary axis.
	 * 
	 * @param secondary the secondary location
	 * @return the slice
	 */
	public final Boolean[] getSecondarySlice(int secondary) {
		return transposed ? solver.getBoard().copyRow(secondary) : solver.getBoard().copyColumn(secondary);
	}
	
}
