/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import name.martingeisse.pixel.common.Picture;
import name.martingeisse.pixel.nonogram.NonogramBoard;

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
		NonogramBoard board = new NonogramBoard(picture, true);
		
		
//		Picture picture = new Picture(5, 3);
//		picture.setPixel(0, 1, true);
//		picture.setPixel(0, 2, false);
//		picture.setPixel(3, 1, true);
		board.renderToPngFile(10, new File("test.png"));
		
	}
	
}
