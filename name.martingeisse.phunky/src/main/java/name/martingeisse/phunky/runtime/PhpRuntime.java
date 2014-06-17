/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

import java.io.PrintWriter;
import java.io.Writer;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;
import name.martingeisse.phunky.runtime.builtin.EchoFunction;
import name.martingeisse.phunky.runtime.builtin.string.ImplodeFunction;
import name.martingeisse.phunky.runtime.builtin.string.StrlenFunction;

/**
 * This class represents the whole PHP runtime environment.
 */
public final class PhpRuntime {

	/**
	 * the globalEnvironment
	 */
	private final Environment globalEnvironment;
	
	/**
	 * the functions
	 */
	private final Functions functions;
	
	/**
	 * the interpreter
	 */
	private final SourceFileInterpreter interpreter;
	
	/**
	 * the outputWriter
	 */
	private PrintWriter outputWriter;

	/**
	 * Constructor for a standard PHP runtime.
	 */
	public PhpRuntime() {
		this(true);
	}

	/**
	 * Constructor.
	 * @param standardDefinitions whether to apply standard definitions.
	 * Passing true here has the same effect as calling {@link #applyStandardDefinitions()}.
	 */
	public PhpRuntime(boolean standardDefinitions) {
		this.globalEnvironment = new Environment(this);
		this.functions = new Functions(this);
		this.interpreter = new SourceFileInterpreter(this);
		this.outputWriter = new PrintWriter(System.out);
		if (standardDefinitions) {
			applyStandardDefinitions();
		}
	}
	
	/**
	 * Getter method for the globalEnvironment.
	 * @return the globalEnvironment
	 */
	public Environment getGlobalEnvironment() {
		return globalEnvironment;
	}

	/**
	 * Getter method for the functions.
	 * @return the functions
	 */
	public Functions getFunctions() {
		return functions;
	}

	/**
	 * Getter method for the interpreter.
	 * @return the interpreter
	 */
	public SourceFileInterpreter getInterpreter() {
		return interpreter;
	}
	
	/**
	 * Getter method for the outputWriter.
	 * @return the outputWriter
	 */
	public PrintWriter getOutputWriter() {
		return outputWriter;
	}
	
	/**
	 * Setter method for the outputWriter.
	 * @param outputWriter the outputWriter to set
	 */
	public void setOutputWriter(PrintWriter outputWriter) {
		this.outputWriter = outputWriter;
	}
	
	/**
	 * Setter method for the outputWriter. This method wraps
	 * the specified writer with a {@link PrintWriter}.
	 * @param outputWriter the outputWriter to set
	 */
	public void setOutputWriter(Writer outputWriter) {
		this.outputWriter = new PrintWriter(outputWriter);
	}
	
	/**
	 * Flushes any pending output.
	 */
	public void flushOutputWriter() {
		outputWriter.flush();
	}
	
	/**
	 * Applies standard definitions to this runtime
	 */
	public void applyStandardDefinitions() {
		addBuiltinCallables(new EchoFunction());
		addBuiltinCallables(new StrlenFunction());
		addBuiltinCallables(new ImplodeFunction());
	}
	
	/**
	 * 
	 */
	private void addBuiltinCallables(BuiltinCallable... callables) {
		for (BuiltinCallable callable : callables) {
			this.functions.put(callable.getName(), callable);
		}
	}
	
	/**
	 * Called when an error occurs. This message prints the error to System.err.
	 * @param message the error message
	 */
	public void triggerError(String message) {
		System.err.println(message);
	}
	
}
