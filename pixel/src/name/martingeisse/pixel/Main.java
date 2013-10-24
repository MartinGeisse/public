/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import name.martingeisse.pixel.common.Picture;
import name.martingeisse.pixel.nonogram.NonogramBoard;
import name.martingeisse.pixel.nonogram.NonogramSolver;
import name.martingeisse.pixel.nonogram.fast.NonogramFastStrategy;
import name.martingeisse.pixel.nonogram.misc.NonogramStarterStrategy;
import name.martingeisse.pixel.nonogram.misc.SliceSpanCombinationsStrategy;

/**
 * Ever-changing main program.
 */
public class Main {

	/**
	 * Main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
	
		BufferedImage inputImage = ImageIO.read(new File("testpic1.png"));
		Picture picture = new Picture(inputImage);
		NonogramBoard board = new NonogramBoard(picture, false);
		NonogramSolver solver = new NonogramSolver(board);
		
		new NonogramStarterStrategy().run(solver);
		new NonogramFastStrategy().run(solver);
		new SliceSpanCombinationsStrategy().run(solver);
		
		board.renderToPngFile(20, true, new File("test.png"));
		
	}
	
}
