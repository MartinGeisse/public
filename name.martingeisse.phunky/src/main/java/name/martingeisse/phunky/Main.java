/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky;

import java.io.File;

import name.martingeisse.phunky.runtime.PhpRuntime;

/**
 * The main class.
 */
public final class Main {

	/**
	 * The main method.
	 * @param args command-line arguments (currently ignored)
	 */
	public static void main(String[] args) {
		PhpRuntime runtime = new PhpRuntime();
		runtime.applyStandardDefinitions();
//		for (File file : new File("samples").listFiles()) {
//			testSample(runtime, file);
//		}
		testSample(runtime, new File("samples/switch-with-early-default.php"));
	}

	private static void testSample(PhpRuntime runtime, File file) {
		System.out.println();
		System.out.println("---------------------------------------------------------------");
		System.out.println("---------------------------------------------------------------");
		System.out.println("---------------------------------------------------------------");
		System.out.println();
		System.out.println("testing: " + file.getPath());
		try {
			runtime.getInterpreter().dump(file);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return;
		}
		System.out.println("---------------------------------------------------------------");
		try {
			runtime.getInterpreter().execute(file);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		runtime.flushOutputWriter();
		System.out.println();
		System.out.println("done");
	}
	
}
