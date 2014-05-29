/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.tools;

/**
 * Generates the Lexer.
 */
public final class LexerGeneratorMain {

	/**
	 * The main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		String[] jflexArgs = {
			"-d", "src/main/java/name/martingeisse/phunky/runtime/parser",
			"--noinputstreamctor",
			"src/main/java/name/martingeisse/phunky/runtime/parser/lexer.jflex",
		};
		jflex.Main.main(jflexArgs);
	}
	
}
