/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky;

import java.io.File;

import name.martingeisse.phunky.runtime.PhpRuntime;

/**
 * The main class.
 */
public final class Main2 {

	/**
	 * The main method.
	 * @param args command-line arguments (currently ignored)
	 */
	public static void main(String[] args) {
		PhpRuntime runtime = new PhpRuntime();
		runtime.applyStandardDefinitions();
		try {
			runtime.getInterpreter().execute(new File("/Users/geisse/phunky-test/app/webroot/index.php"));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		runtime.flushOutputWriter();
		System.out.println();
	}
	
}