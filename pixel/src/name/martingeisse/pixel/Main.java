/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import name.martingeisse.pixel.common.Picture;
import name.martingeisse.pixel.nonogram.NonogramBoard;
import name.martingeisse.pixel.nonogram.fast.NonogramFastStrategy;
import name.martingeisse.pixel.nonogram.misc.ForkStrategy;
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
	
		BufferedImage inputImage = ImageIO.read(new File("images/edited/wacken.png"));
		Picture picture = new Picture(inputImage);
		NonogramBoard board = new NonogramBoard(picture, false);
		
		new NonogramStarterStrategy().run(board);
		 for (int j=0; j<500; j++) {
			 new NonogramFastStrategy().run(board);
			new SliceSpanCombinationsStrategy().run(board);
		}
		 /*
		System.out.println("fork 1");
		new ForkStrategy(1).run(board);
		for (int j=0; j<500; j++) {
			new NonogramFastStrategy().run(board);
			new SliceSpanCombinationsStrategy().run(board);
		}
		new ForkStrategy(1).run(board);
		for (int j=0; j<500; j++) {
			new NonogramFastStrategy().run(board);
			new SliceSpanCombinationsStrategy().run(board);
		}
		new ForkStrategy(1).run(board);
		for (int j=0; j<500; j++) {
			new NonogramFastStrategy().run(board);
			new SliceSpanCombinationsStrategy().run(board);
		}
		*/

		
		
		// System.out.println("fork 2");
		// new ForkStrategy(2).run(board);
		
		board.renderToPngFile(20, true, new File("test.png"));
		
	}
	
}
