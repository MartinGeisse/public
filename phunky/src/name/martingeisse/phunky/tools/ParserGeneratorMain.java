/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.tools;

/**
 * Generates the Parser.
 */
public final class ParserGeneratorMain {

	/**
	 * The main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		// generate the parser
		String[] cupArgs = {
			"-package", "name.martingeisse.phunky.runtime.parser",
			"-destdir", "src/name/martingeisse/phunky/runtime/parser",
			"-parser", "Parser",
			"-symbols", "Tokens",
			"src/name/martingeisse/phunky/runtime/parser/grammar.cup",
		};
		java_cup.Main.main(cupArgs);
		// java_cup.Main.main(new String[] {"--help"});
	}
	
}
