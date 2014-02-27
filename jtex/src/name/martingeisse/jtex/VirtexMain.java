/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex;

import tex.Tex;

/**
 * Runs the "virtex" program, i.e. tex with a pre-loaded format file.
 */
public final class VirtexMain {

	/**
	 * Main method.
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		Tex tex = new Tex(false, args);
		tex.run();
	}
	
}
