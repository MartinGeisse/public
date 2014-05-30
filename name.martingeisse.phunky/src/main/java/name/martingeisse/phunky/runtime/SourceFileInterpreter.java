/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java_cup.runtime.ComplexSymbolFactory;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.statement.StatementSequence;
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
		parse(sourceFile).execute(runtime.getGlobalEnvironment());
	}

	/**
	 * Loads and dumps a source file. This is used mainly to test the parser.
	 * @param sourceFile the source file
	 */
	public void dump(final File sourceFile) {
		CodeDumper dumper = new CodeDumper();
		parse(sourceFile).dump(dumper);
		System.out.println(dumper.toString());
	}

	/**
	 * Parses a source file. The file MUST be in UTF-8 encoding.
	 * 
	 * @param sourceFile the file to parse
	 * @return the AST
	 */
	private StatementSequence parse(final File sourceFile) {
		try (FileInputStream fileInputStream = new FileInputStream(sourceFile); InputStreamReader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
			Lexer lexer = new Lexer(reader);
			Parser parser = new Parser(lexer, new ComplexSymbolFactory());
			StatementSequence program = (StatementSequence)parser.parse().value;
			return program;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
