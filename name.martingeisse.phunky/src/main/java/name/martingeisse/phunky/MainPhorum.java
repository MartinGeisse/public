/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky;

import java.io.File;

import name.martingeisse.phunky.runtime.PhpRuntime;

/**
 * The main class.
 */
public final class MainPhorum {

	/**
	 * The main method.
	 * @param args command-line arguments (currently ignored)
	 */
	public static void main(String[] args) {
		PhpRuntime runtime = new PhpRuntime();
		runtime.applyStandardDefinitions();
		try {
			runtime.getInterpreter().execute(new File("/Users/geisse/Sites/phorum/index.php"));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		runtime.flushOutputWriter();
		System.out.println();
	}
	
}
