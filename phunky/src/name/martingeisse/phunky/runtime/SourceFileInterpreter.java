/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import name.martingeisse.phunky.runtime.code.Expression;
import name.martingeisse.phunky.runtime.parser.Lexer;
import name.martingeisse.phunky.runtime.parser.Parser;

/**
 * This class makes the connection between PHP source files and the
 * PHP runtime.
 */
public final class SourceFileInterpreter {

	/**
	 * the runtime
	 */
	private final PhpRuntime runtime;

	/**
	 * Constructor.
	 * @param runtime the PHP runtime
	 */
	SourceFileInterpreter(final PhpRuntime runtime) {
		this.runtime = runtime;
	}

	/**
	 * Getter method for the runtime.
	 * @return the runtime
	 */
	public PhpRuntime getRuntime() {
		return runtime;
	}

	/**
	 * Loads and executes a source file.
	 * @param sourceFile the source file
	 */
	public void execute(final File sourceFile) {
		try (FileInputStream fileInputStream = new FileInputStream(sourceFile); InputStreamReader reader = new InputStreamReader(fileInputStream)) {
			Lexer lexer = new Lexer(reader);
			Parser parser = new Parser(lexer, new ComplexSymbolFactory());
			Symbol program = parser.parse();
			Expression expression = (Expression)program.value;
			Object value = expression.evaluate(runtime.getGlobalEnvironment());
			System.out.println("value of " + sourceFile + " = " + value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
