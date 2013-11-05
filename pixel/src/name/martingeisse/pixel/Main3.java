/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel;


/**
 * Ever-changing main program.
 */
public class Main3 {

	/**
	 * Main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {

		String filename = "23.png";
		PuzzleHandler handler = new PuzzleHandler(filename, "C-");
		
		int hintStartX = 3, hintStepX = 5, hintEndX = handler.getBoard().getWidth();
		int hintStartY = 3, hintStepY = 5, hintEndY = handler.getBoard().getHeight();
		for (int i = hintStartX; i < hintEndX; i += hintStepX) {
			for (int j = hintStartY; j < hintEndY; j += hintStepY) {
				handler.addHint(i, j);
			}
			
		}
//
//		handler.addHintBox(28, 20, 2, 3);
		
		handler.finish();
		
	}

}
