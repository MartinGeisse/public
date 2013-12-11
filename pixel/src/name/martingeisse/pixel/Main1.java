/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel;


/**
 * Ever-changing main program.
 */
public class Main1 {

	/**
	 * Main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {

		String filename = "08.png";
		PuzzleHandler handler = new PuzzleHandler(filename, "A-");
		
//		int hintStartX = 2, hintStepX = 4, hintEndX = handler.getBoard().getWidth();
//		int hintStartY = 2, hintStepY = 4, hintEndY = handler.getBoard().getHeight();
//		for (int i = hintStartX; i < hintEndX; i += hintStepX) {
//			for (int j = hintStartY; j < hintEndY; j += hintStepY) {
//				handler.addHint(i, j);
//			}
//		}
//
//		handler.addHintBox(0, 45, 40, 15);
//		handler.addHintBox(50, 0, 15, 20);
//		handler.addHintRow(14);
//		handler.addHintRow(18);
//		handler.addHintRow(25);
//		handler.addHintRow(39);
		
		handler.finish();
		
	}

}
