/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
	 * Constructor.
	 * @param width the width in image pixels
	 * @param height the height in image pixels
	 * @param cellSize the cell size
	 */
	public DrawHelper(int width, int height, int cellSize) {
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		this.cellSize = cellSize;
		this.graphics = image.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		graphics.setColor(Color.black);
	}
	
	/**
	 * Getter method for the image.
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
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
		graphics.fillRect(cellX * cellSize, cellY * cellSize, cellSize, cellSize);
	}

	/**
	 * Draws a "left white" mark.
	 * @param cellX the x position in cells
	 * @param cellY the y position in cells
	 */
	public void drawLeftWhiteMark(int cellX, int cellY) {
		int x = cellX * cellSize;
		int y = cellY * cellSize;
		int h = cellSize / 2;
		int q = cellSize / 4;
		graphics.drawLine(x + h - q, y + h - q, x + h + q, y + h + q);
		graphics.drawLine(x + h - q, y + h + q, x + h + q, y + h - q);
	}

	/**
	 * Draws a pixel grid.
	 * @param cellsX the number of cells in the x direction
	 * @param cellsY the number of cells in the y direction
	 */
	public void drawGrid(int cellsX, int cellsY) {
		for (int i=0; i<=cellsX; i++) {
			graphics.drawLine(i * cellSize, 0, i * cellSize, cellsY * cellSize);
		}
		for (int i=0; i<=cellsY; i++) {
			graphics.drawLine(0, i * cellSize, cellsX * cellSize, i * cellSize);
		}
	}
	
}
