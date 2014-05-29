/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude;

import java.io.File;
import java.io.IOException;

import name.martingeisse.texturedude.corefunctions.CoreFunctions;

/**
 * Test main program.
 */
public class Main {

	/**
	 * The main method.
	 * @param args command-line arguments (ignored)
	 * @throws IOException on errors
	 */
	public static void main(String[] args) throws IOException {
		
		byte[] program = new byte[] {
			(byte)CoreFunctions.CREATE_LAYER, (byte)256, (byte)192,
			(byte)CoreFunctions.PERLIN_NOISE,
				(byte)255, (byte)0, (byte)0,
				(byte)0, (byte)255, (byte)0,
				(byte)50, (byte)20, (byte)0,
			// (byte)CoreFunctions.FLAT_COLOR, (byte)255, (byte)0, (byte)0,
		};
		TextureDude dude = new TextureDude();
		dude.setProgram(program);
		dude.execute();
		
		dude.getLayerStack().pop().exportPng(new File("test.png"));
	}
	
}
