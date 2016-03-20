/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.simulation.lwjgl;

/**
 * A simulator GUI that uses a matrix of cells and stores a 32-bit
 * integer value for each cell.
 */
public abstract class Int32MatrixSimulatorGui extends GraphicsSimulatorGui {

	/**
	 * the widthInCells
	 */
	private final int widthInCells;

	/**
	 * the heightInCells
	 */
	private final int heightInCells;

	/**
	 * the cellWidthInPixels
	 */
	private final int cellWidthInPixels;

	/**
	 * the cellHeightInPixels
	 */
	private final int cellHeightInPixels;
	
	/**
	 * the matrix
	 */
	private final int[] matrix;

	/**
	 * Constructor.
	 * @param widthInCells the display width in cells
	 * @param heightInCells the display height in cells
	 * @param cellWidthInPixels the cell width in pixels
	 * @param cellHeightInPixels the cell height in pixels
	 */
	public Int32MatrixSimulatorGui(int widthInCells, int heightInCells, int cellWidthInPixels, int cellHeightInPixels) {
		super(widthInCells * cellWidthInPixels, heightInCells * cellHeightInPixels);
		this.widthInCells = widthInCells;
		this.heightInCells = heightInCells;
		this.cellWidthInPixels = cellWidthInPixels;
		this.cellHeightInPixels = cellHeightInPixels;
		this.matrix = new int[widthInCells * heightInCells];
	}

	/**
	 * Getter method for the widthInCells.
	 * @return the widthInCells
	 */
	public int getWidthInCells() {
		return widthInCells;
	}

	/**
	 * Getter method for the heightInCells.
	 * @return the heightInCells
	 */
	public int getHeightInCells() {
		return heightInCells;
	}

	/**
	 * Getter method for the cellWidthInPixels.
	 * @return the cellWidthInPixels
	 */
	public int getCellWidthInPixels() {
		return cellWidthInPixels;
	}

	/**
	 * Getter method for the cellHeightInPixels.
	 * @return the cellHeightInPixels
	 */
	public int getCellHeightInPixels() {
		return cellHeightInPixels;
	}
	
	/**
	 * Reads a cell value from the matrix.
	 * @param x the x position of the cell
	 * @param y the y position of the cell
	 * @return the cell value
	 */
	public int getCellValue(int x, int y) {
		return matrix[getCellIndex(x, y)];
	}
	
	/**
	 * Writes a cell value to the matrix.
	 * @param x the x position of the cell
	 * @param y the y position of the cell
	 * @param value the value to write
	 */
	public void setCellValue(int x, int y, int value) {
		int index = getCellIndex(x, y);
		if (matrix[index] != value) {
			requestRedraw();
		}
		matrix[index] = value;
	}

	/**
	 * 
	 */
	private int getCellIndex(int x, int y) {
		checkCellPositionBounds(x, y);
		return (y * widthInCells + x);
	}
	
	/**
	 * 
	 */
	private void checkCellPositionBounds(int x, int y) {
		if (x < 0 || x >= widthInCells || y < 0 || y >= heightInCells) {
			throw new IllegalArgumentException("invalid cell position: " + x + ", " + y + " (size: " + widthInCells + " x " + heightInCells + ")");
		}
	}
}
