/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex;

import tex.Tex;

/**
 * Runs the "initex" program, i.e. a version of TeX that generates
 * a format file.
 */
public final class InitexMain {

	/**
	 * Main method.
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		Tex tex = new Tex(args);
		tex.run();
	}
	
}
