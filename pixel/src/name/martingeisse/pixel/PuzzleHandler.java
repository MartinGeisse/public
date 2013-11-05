/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel;

import java.io.File;

import javax.imageio.ImageIO;

import name.martingeisse.pixel.common.Picture;
import name.martingeisse.pixel.nonogram.NonogramBoard;
import name.martingeisse.pixel.nonogram.misc.ForkStrategy;
import name.martingeisse.pixel.nonogram.misc.NonForkingStrategyBundle;
import name.martingeisse.pixel.nonogram.misc.NonogramEmptySliceStrategy;
import name.martingeisse.pixel.nonogram.misc.NonogramStarterStrategy;

/**
 * Helper to run multiple main programs at once.
 */
public class PuzzleHandler {

	/**
	 * the picture
	 */
	private Picture picture;

	/**
	 * the board
	 */
	private NonogramBoard board;

	/**
	 * the outputPrefix
	 */
	private String outputPrefix;
	
	/**
	 * Constructor.
	 * @param filename the picture filename
	 * @param outputPrefix the prefix for output files
	 * @throws Exception on errors
	 */
	public PuzzleHandler(String filename, String outputPrefix) throws Exception {
		this.picture = new Picture(ImageIO.read(new File("images/final/" + filename)));
		this.board = new NonogramBoard(picture, false);
		this.outputPrefix = outputPrefix;
	}
	
	/**
	 * Getter method for the picture.
	 * @return the picture
	 */
	public Picture getPicture() {
		return picture;
	}
	
	/**
	 * Getter method for the board.
	 * @return the board
	 */
	public NonogramBoard getBoard() {
		return board;
	}
	
	/**
	 * @throws Exception ...
	 */
	public void finish() throws Exception {
		new NonogramEmptySliceStrategy().run(board);
		picture.renderToPngFile(20, true, new File(outputPrefix + "picture.png"));
		board.renderToPngFile(20, true, new File(outputPrefix + "board.png"));
		solve();
		for (int x=0; x<board.getWidth(); x++) {
			for (int y=0; y<board.getHeight(); y++) {
				if (board.getPixel(x, y) == null) {
					System.out.println("UNKNOWN: " + x + ", " + y);
				}
			}
		}
		board.renderToPngFile(20, true, new File(outputPrefix +"real-solved.png"));
		board = new NonogramBoard(picture, true);
		board.renderToPngFile(20, true, new File(outputPrefix +"magic-solved.png"));
		
	}

	private void solve() {
		new NonogramStarterStrategy().run(board);
		while (true) {
			System.out.println("--- solve() round ---");
			int counter = board.getChangeCounter();
			System.out.println("counter: " + counter + ". trying non-forking bundle...");
			new NonForkingStrategyBundle().run(board);
			System.out.println("counter: " + board.getChangeCounter() + ". trying forking bundle...");
			new ForkStrategy(1).run(board);
			if (counter == board.getChangeCounter()) {
				break;
			}
		}
	}

	/**
	 * @param x ...
	 * @param y ...
	 */
	public void addHint(final int x, final int y) {
		board.setPixel(x, y, picture.getPixel(x, y));
	}

	/**
	 * @param x ...
	 */
	public void addHintColumn(final int x) {
		for (int i=0; i<board.getHeight(); i++) {
			addHint(x, i);
		}
	}
	
	/**
	 * @param y ...
	 */
	public void addHintRow(final int y) {
		for (int i=0; i<board.getWidth(); i++) {
			addHint(i, y);
		}
	}

	/**
	 * @param x ...
	 * @param y ...
	 * @param w ...
	 * @param h ...
	 */
	public void addHintBox(int x, int y, int w, int h) {
		for (int i=0; i<w; i++) {
			for (int j=0; j<h; j++) {
				addHint(x + i, y + j);
			}
		}
	}

}
