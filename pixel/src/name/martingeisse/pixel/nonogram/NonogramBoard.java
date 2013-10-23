/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram;

import name.martingeisse.pixel.common.AbstractBoard;
import name.martingeisse.pixel.common.Picture;

/**
 * Game board for nonograms.
 */
public final class NonogramBoard extends AbstractBoard {

	/**
	 * Constructor.
	 * @param picture the picture to create an instance from
	 * @param solved true to initialize the board pixels with the
	 * picture's pixels, false to leave the board clean
	 */
	public NonogramBoard(Picture picture, boolean solved) {
		super(picture, solved);
	}

}
