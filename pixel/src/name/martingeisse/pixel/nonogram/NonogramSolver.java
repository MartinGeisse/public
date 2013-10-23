/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram;

/**
 * Helper class to solve nonogram puzzles.
 */
public class NonogramSolver {

	/**
	 * the board
	 */
	private final NonogramBoard board;

	/**
	 * Constructor.
	 * @param board the board
	 */
	public NonogramSolver(NonogramBoard board) {
		this.board = board;
	}

	/**
	 * Getter method for the board.
	 * @return the board
	 */
	public NonogramBoard getBoard() {
		return board;
	}
	
	
}
