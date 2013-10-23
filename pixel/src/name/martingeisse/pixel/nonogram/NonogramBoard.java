/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram;

import name.martingeisse.pixel.common.AbstractBoard;
import name.martingeisse.pixel.common.DrawHelper;
import name.martingeisse.pixel.common.Picture;

/**
 * Game board for nonograms.
 * 
 * Note that the hints stored in this object should not be modified.
 */
public final class NonogramBoard extends AbstractBoard {

	/**
	 * the rowHints
	 */
	private final int[][] rowHints;
	
	/**
	 * the columnHints
	 */
	private final int[][] columnHints;
	
	/**
	 * Constructor.
	 * @param picture the picture to create an instance from
	 * @param solved true to initialize the board pixels with the
	 * picture's pixels, false to leave the board clean
	 */
	public NonogramBoard(Picture picture, boolean solved) {
		super(picture, solved);
		rowHints = buildHints(picture, true);
		columnHints = buildHints(picture, false);
	}
	
	/**
	 * 
	 */
	private int[][] buildHints(Picture picture, boolean trueForRowsFalseForColumns) {
		int numberOfEntries = (trueForRowsFalseForColumns ? getHeight() : getWidth());
		int totalRange = (trueForRowsFalseForColumns ? getWidth() : getHeight());
		int[][] result = new int[numberOfEntries][];
		for (int i=0; i<numberOfEntries; i++) {
			
			// count the number of spans
			int spanCount = 0;
			{
				boolean previous = false;
				for (int j=0; j<totalRange; j++) {
					boolean current = getPicturePixel(picture, i, j, trueForRowsFalseForColumns);
					if (current && !previous) {
						spanCount++;
					}
					previous = current;
				}
			}
			
			// create a hint entry
			int[] entry = result[i] = new int[spanCount];
			
			// measure span length
			{
				int currentSpan = 0;
				boolean previous = false;
				for (int j=0; j<totalRange; j++) {
					boolean current = getPicturePixel(picture, i, j, trueForRowsFalseForColumns);
					if (current) {
						entry[currentSpan]++;
					} else if (previous) {
						currentSpan++;
					}
					previous = current;
				}
			}
			
		}
		return result;
	}
	
	private boolean getPicturePixel(Picture picture, int primary, int secondary, boolean trueForRowsFalseForColumns) {
		if (trueForRowsFalseForColumns) {
			return picture.getPixel(secondary, primary);
		} else {
			return picture.getPixel(primary, secondary);
		}
	}
	
	/**
	 * Getter method for the rowHints.
	 * @return the rowHints
	 */
	public int[][] getRowHints() {
		return rowHints;
	}
	
	/**
	 * @return the greatest number of row hints for all rows
	 */
	public int getMaxRowHintLength() {
		int result = 0;
		for (int[] rowHintsRow : rowHints) {
			if (rowHintsRow.length > result) {
				result = rowHintsRow.length;
			}
		}
		return result;
	}
	
	/**
	 * Getter method for the columnHints.
	 * @return the columnHints
	 */
	public int[][] getColumnHints() {
		return columnHints;
	}

	/**
	 * @return the greatest number of column hints for all column
	 */
	public int getMaxColumnHintLength() {
		int result = 0;
		for (int[] columnHintsColumn : columnHints) {
			if (columnHintsColumn.length > result) {
				result = columnHintsColumn.length;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.common.AbstractMatrix#renderToDrawHelper(int, boolean)
	 */
	@Override
	public DrawHelper renderToDrawHelper(int cellSize, boolean grid) {
		
		// prepare a draw helper
		int width = getWidth();
		int height = getHeight();
		int maxRowHintLength = getMaxRowHintLength();
		int maxColumnHintLength = getMaxColumnHintLength();
		int gridExtra = (grid ? 1 : 0);
		DrawHelper helper = new DrawHelper((width + maxRowHintLength) * cellSize + gridExtra, (height + maxColumnHintLength) * cellSize + gridExtra, cellSize);
		helper.setCellOffsetX(maxRowHintLength);
		helper.setCellOffsetY(maxColumnHintLength);
		
		// render the main board area
		renderPixels(helper, grid);
		
		// render the hints
		renderHints(helper, true);
		renderHints(helper, false);
		
		return helper;
	}

	/**
	 * 
	 */
	private void renderHints(DrawHelper helper, boolean trueForRowsFalseForColumns) {
		int[][] hints = (trueForRowsFalseForColumns ? rowHints : columnHints);
		for (int i=0; i<hints.length; i++) {
			int[] hintsEntry = hints[i];
			for (int j=0; j<hintsEntry.length; j++) {
				int x = (trueForRowsFalseForColumns ? -1-j : i);
				int y = (trueForRowsFalseForColumns ? i : -1-j);
				helper.drawNumber(x, y, hintsEntry[j]);
			}
		}
	}
	
}
