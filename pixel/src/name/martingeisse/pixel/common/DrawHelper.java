/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Helper to draw pictures and boards.
 */
public final class DrawHelper {

	/**
	 * the image
	 */
	private final BufferedImage image;

	/**
	 * the cellSize
	 */
	private final int cellSize;

	/**
	 * the graphics
	 */
	private final Graphics graphics;

	/**
	 * the cellOffsetX
	 */
	private int cellOffsetX;

	/**
	 * the cellOffsetY
	 */
	private int cellOffsetY;

	/**
	 * Constructor.
	 * @param width the width in image pixels
	 * @param height the height in image pixels
	 * @param cellSize the cell size
	 */
	public DrawHelper(int width, int height, int cellSize) {
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		this.cellSize = cellSize;
		this.graphics = image.getGraphics();
		this.cellOffsetX = 0;
		this.cellOffsetY = 0;
		
		// configure colors
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		graphics.setColor(Color.black);
		
		// configure font
		Map<TextAttribute, Object> fontAttributes = new HashMap<TextAttribute, Object>();
		fontAttributes.put(TextAttribute.TRACKING, -0.1);
		fontAttributes.put(TextAttribute.SIZE, cellSize * 0.8);
		graphics.setFont(graphics.getFont().deriveFont(fontAttributes));

	}

	/**
	 * Getter method for the image.
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Getter method for the cellOffsetX.
	 * @return the cellOffsetX
	 */
	public int getCellOffsetX() {
		return cellOffsetX;
	}

	/**
	 * Setter method for the cellOffsetX.
	 * @param cellOffsetX the cellOffsetX to set
	 */
	public void setCellOffsetX(int cellOffsetX) {
		this.cellOffsetX = cellOffsetX;
	}

	/**
	 * Getter method for the cellOffsetY.
	 * @return the cellOffsetY
	 */
	public int getCellOffsetY() {
		return cellOffsetY;
	}

	/**
	 * Setter method for the cellOffsetY.
	 * @param cellOffsetY the cellOffsetY to set
	 */
	public void setCellOffsetY(int cellOffsetY) {
		this.cellOffsetY = cellOffsetY;
	}

	/**
	 * Writes the image to a PNG file.
	 * @param file the file to write to
	 * @throws IOException on I/O errors
	 */
	public void writePngFile(File file) throws IOException {
		ImageIO.write(image, "png", file);
	}

	/**
	 * Disposes of the graphics context.
	 */
	public void dispose() {
		graphics.dispose();
	}

	/**
	 * Draws a black pixel.
	 * @param cellX the x position in cells
	 * @param cellY the y position in cells
	 */
	public void drawBlackPixel(int cellX, int cellY) {
		graphics.fillRect((cellX + cellOffsetX) * cellSize, (cellY + cellOffsetY) * cellSize, cellSize, cellSize);
	}

	/**
	 * Draws a "left white" mark.
	 * @param cellX the x position in cells
	 * @param cellY the y position in cells
	 */
	public void drawLeftWhiteMark(int cellX, int cellY) {
		int x = (cellX + cellOffsetX) * cellSize;
		int y = (cellY + cellOffsetY) * cellSize;
		int h = cellSize / 2;
		int q = cellSize / 4;
		graphics.drawLine(x + h - q, y + h - q, x + h + q, y + h + q);
		graphics.drawLine(x + h - q, y + h + q, x + h + q, y + h - q);
	}

	/**
	 * Draws a number.
	 * @param cellX the x position in cells
	 * @param cellY the y position in cells
	 * @param n the number to draw
	 */
	public void drawNumber(int cellX, int cellY, int n) {
		String s = Integer.toString(n);
		int xshift = s.length() == 1 ? (cellSize/3) : 0;
		int yshift = graphics.getFontMetrics().getAscent();
		graphics.drawString(s, (cellX + cellOffsetX) * cellSize + xshift, (cellY + cellOffsetY) * cellSize + yshift);
	}
	
	/**
	 * Draws a pixel grid.
	 * @param cellsX the number of cells in the x direction
	 * @param cellsY the number of cells in the y direction
	 */
	public void drawGrid(int cellsX, int cellsY) {
		int baseX = cellOffsetX * cellSize;
		int baseY = cellOffsetY * cellSize;
		for (int i = 0; i <= cellsX; i++) {
			graphics.drawLine(baseX + i * cellSize, baseY, baseX + i * cellSize, baseY + cellsY * cellSize);
		}
		for (int i = 0; i <= cellsY; i++) {
			graphics.drawLine(baseX, baseY + i * cellSize, baseX + cellsX * cellSize, baseY + i * cellSize);
		}
	}

}
