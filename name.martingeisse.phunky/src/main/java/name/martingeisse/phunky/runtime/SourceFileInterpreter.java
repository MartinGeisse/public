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
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.code.statement.StatementSequence;
import name.martingeisse.phunky.runtime.parser.Lexer;
import name.martingeisse.phunky.runtime.parser.Parser;

import org.apache.log4j.Logger;

/**
 * This class makes the connection between PHP source files and the
 * PHP runtime.
 */
public final class SourceFileInterpreter {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(SourceFileInterpreter.class);
	
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
		logger.debug("trying to parse file: " + sourceFile+ ", loading...");
		try (FileInputStream fileInputStream = new FileInputStream(sourceFile); InputStreamReader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
			logger.trace("file loaded.");
			Lexer lexer = new Lexer(reader);
			lexer.setFilePath(sourceFile.getPath());
			Parser parser = new Parser(lexer, new ComplexSymbolFactory());
			StatementSequence program;
			try {
				program = (StatementSequence)parser.parse().value;
			} catch (Exception e) {
				String message = "error parsing file: " + sourceFile;
				logger.debug(message);
				throw new RuntimeException(message, e);
			}
			logger.debug("file parsed: " + sourceFile);
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
			StatementSequence program = parse(sourceFile);
			logger.debug("executing file: " + sourceFile);
			program.execute(runtime.getGlobalEnvironment());
			logger.debug("file terminated normally: " + sourceFile);
			// TODO exit handler
		} catch (ExitException e) {
			logger.info("file exited: " + sourceFile + ", status code: " + e.getStatusCode());
			// TODO exit handler
		} catch (FatalErrorException e) {
			logger.error("file triggered a fatal error: " + sourceFile, e);
			// TODO exit handler
		} catch (Exception e) {
			logger.error("file threw an unhandled exception", e);
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
	 * 
	 * TODO merge execute() and include() ? At least make execute() and exception-handling
	 * wrapper around include, but keep the actual parse/execute logic in include() only.
	 * 
	 * @param callerLocation the caller's location, used to trigger errors
	 * @param path the path to the file to include
	 * @param once whether to skip the file if it has been included before
	 * TODO: what about different paths to the same file?
	 * @param required whether to trigger an error if the file cannot be loaded
	 */
	public void include(CodeLocation callerLocation, String path, boolean once, boolean required) {
		logger.debug("including file: " + path);

		// handle the "once" flag
		if (once && alreadyIncludedFiles.contains(path)) {
			logger.debug("including 'once' and file is already included.");
			return;
		}
		alreadyIncludedFiles.add(path);
		File file = new File(currentSourceFile.getParentFile(), path);
		logger.trace("file resolved to: " + file);
		
		// parse the file
		StatementSequence program;
		try {
			program = parse(file);
		} catch (IOException e) {
			logger.debug("could not load or parse file: " + file);
			if (required) {
				runtime.triggerError("failed to load required source file '" + file + "'", callerLocation);
			}
			return;
		}
		
		// execute the statements in it
		File previousSourceFile = currentSourceFile;
		try {
			currentSourceFile = file;
			program.execute(runtime.getGlobalEnvironment());
			logger.debug("file executed: " + file);
		} catch (RuntimeException e) {
			logger.debug("exception while executing file: " + file, e);
			throw e;
		} finally {
			currentSourceFile = previousSourceFile;
		}
		
	}

}
