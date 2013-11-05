/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel;

import java.io.File;

import javax.imageio.ImageIO;

import name.martingeisse.pixel.common.Picture;


/**
 * Ever-changing main program.
 */
public class ExperimentMain {

	/**
	 * Main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {

		String filename = "19.png";
		Picture original = new Picture(ImageIO.read(new File("images/final/" + filename)));
		original.renderToPngFile(1, false, new File("19-original.png"));
		
		Picture key = new Picture(original.getWidth(), original.getHeight());
		key.random(0);
		key.scaleToCheckerboards().scaleUp(3).renderToPngFile(1, false, new File("19-checker-key.png"));

		Picture ciphertext = original.scaleUp(1);
		ciphertext.mergeXor(key);
		ciphertext.scaleToCheckerboards().scaleUp(3).renderToPngFile(1, false, new File("19-checker-ciphertext.png"));
		
//		Picture test = ciphertext.scaleUp(1).scaleToCheckerboards();
//		test.merge(key.scaleToCheckerboards(), true);
//		test.renderToPngFile(1, false, new File("19-checker-test.png"));
		
		
//		upscaled.invert();
//		upscaled.merge(original, false);
//		upscaled.renderToPngFile(1, false, new File("diff.png"));
		
	}

}
