/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import name.martingeisse.api.handler.misc.NoFaviconDecorator;
import name.martingeisse.api.servlet.Launcher;

/**
 * Main class.
 */
public class Main {

	/**
	 * The main method
	 * @param args ...
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		Launcher.launch(new NoFaviconDecorator(new ApplicationHandler()), null);
	}
	
}
