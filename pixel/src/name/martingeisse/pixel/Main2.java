/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel;


/**
 * Ever-changing main program.
 */
public class Main2 {

	/**
	 * Main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {

		String filename = "22.png";
		PuzzleHandler handler = new PuzzleHandler(filename, "B-");
		
//		int hintStartX = 2, hintStepX = 5, hintEndX = handler.getBoard().getWidth();
//		int hintStartY = 2, hintStepY = 5, hintEndY = handler.getBoard().getHeight();
//		for (int i = hintStartX; i < hintEndX; i += hintStepX) {
//			for (int j = hintStartY; j < hintEndY; j += hintStepY) {
//				handler.addHint(i, j);
//			}
//		}
//		handler.addHintRow(13);
//		handler.addHintRow(19);
//		handler.addHintRow(26);
//		handler.addHintColumn(12);
//		handler.addHintColumn(16);
//		handler.addHintColumn(29);
//		handler.addHintBox(15, 25, 10, 10);
//		handler.addHint(21, 21);

//		handler.addHintBox(0, 0, 10, 5);
//		handler.addHintBox(25, 0, 15, 5);
//		handler.addHintBox(0, 20, 5, 40);
//		handler.addHintBox(30, 35, 10, 15);
//		handler.addHintBox(20, 50, 5, 5);

		handler.addHintColumn(19);
		handler.addHintColumn(20);
		handler.addHintRow(14);
		handler.addHintRow(17);
		
		handler.addHintBox(10, 10, 5, 5);
		handler.addHintBox(20, 35, 5, 5);
		handler.addHintBox(20, 50, 5, 5);

		handler.addHint(9, 13);
		
		handler.finish();
		
	}

}
