/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
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
	 * the alreadyIncludedFiles
	 */
	private final Set<String> alreadyIncludedFiles;
	
	/**
	 * the currentSourceFile
	 */
	private File currentSourceFile;

	/**
	 * Constructor.
	 * @param runtime the PHP runtime
	 */
	SourceFileInterpreter(final PhpRuntime runtime) {
		this.runtime = runtime;
		this.alreadyIncludedFiles = new HashSet<>();
	}

	/**
	 * Getter method for the runtime.
	 * @return the runtime
	 */
	public PhpRuntime getRuntime() {
		return runtime;
	}

	/**
	 * Parses a source file. The file MUST be in UTF-8 encoding.
	 * 
	 * @param sourceFile the file to parse
	 * @return the AST
	 */
	private StatementSequence parse(final File sourceFile) throws IOException {
		try (FileInputStream fileInputStream = new FileInputStream(sourceFile); InputStreamReader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
			Lexer lexer = new Lexer(reader);
			Parser parser = new Parser(lexer, new ComplexSymbolFactory());
			StatementSequence program;
			try {
				program = (StatementSequence)parser.parse().value;
			} catch (Exception e) {
				throw new RuntimeException("could not parse " + sourceFile);
			}
			return program;
		}
	}

	/**
	 * Loads and executes a source file.
	 * @param sourceFile the source file
	 */
	public void execute(final File sourceFile) {
		File previousSourceFile = currentSourceFile;
		try {
			currentSourceFile = sourceFile;
			parse(sourceFile).execute(runtime.getGlobalEnvironment());
			// TODO exit handler
		} catch (ExitException e) {
			// TODO exit handler
		} catch (FatalErrorException e) {
			// TODO exit handler
		} catch (Exception e) {
			// TODO exit handler
			// TODO distinguish PHP exceptions and Java exceptions
		} finally {
			currentSourceFile = previousSourceFile;
		}
	}

	/**
	 * Loads and dumps a source file. This is used mainly to test the parser.
	 * @param sourceFile the source file
	 * @throws IOException on I/O errors
	 */
	public void dump(final File sourceFile) throws IOException {
		CodeDumper dumper = new CodeDumper();
		parse(sourceFile).dump(dumper);
		System.out.println(dumper.toString());
	}
	
	/**
	 * Includes a source code file.
	 * @param path the path to the file to include
	 * @param once whether to skip the file if it has been included before
	 * TODO: what about different paths to the same file?
	 * @param required whether to trigger an error if the file cannot be loaded
	 */
	public void include(String path, boolean once, boolean required) {
		
		// handle the "once" flag
		if (once && alreadyIncludedFiles.contains(path)) {
			return;
		}
		alreadyIncludedFiles.add(path);
		File file = new File(currentSourceFile.getParentFile(), path);
		
		// parse the file
		StatementSequence program;
		try {
			program = parse(file);
		} catch (IOException e) {
			String message = "failed to load required source file '" + file + "'";
			if (required) {
				runtime.triggerError(message);
			} else {
				System.out.println("info: "+ message);
			}
			return;
		}
		
		// execute the statements in it
		File previousSourceFile = currentSourceFile;
		try {
			currentSourceFile = file;
			program.execute(runtime.getGlobalEnvironment());
		} finally {
			currentSourceFile = previousSourceFile;
		}
		
	}

}
