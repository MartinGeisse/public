/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

import org.apache.log4j.Logger;

/**
 * This class is used by the runtime to log execution of PHP scripts.
 */
public final class RuntimeLog {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(RuntimeLog.class);
	
	/**
	 * Logs the beginning of executing a source code file.
	 * @param path the path to the source file
	 */
	public void beginSourceFile(String path) {
		log("begin source file: " + path);
	}
	
	/**
	 * Logs the end of executing a source code file.
	 * @param path the path to the source file
	 */
	public void endSourceFile(String path) {
		log("end source file: " + path);
	}
	
	/**
	 * Logs the beginning of a statement execution.
	 * @param name the name of the statement being executed
	 */
	public void beginStatement(String name) {
		log("begin statement: " + name);
	}
	
	/**
	 * Logs the end of a statement execution.
	 * @param name the name of the statement being executed
	 */
	public void endStatement(String name) {
		log("end statement: " + name);
	}
	
	/**
	 * Logs the end of a statement execution.
	 * @param name the name of the statement being executed
	 * @param info additional information about ending the statement
	 */
	public void endStatement(String name, String info) {
		log("end statement: " + name + " (" + info + ")");
	}
	
	/**
	 * Logs the beginning of an expression evaluation.
	 * @param name the name of the expression being evaluated
	 */
	public void beginExpression(String name) {
		log("begin expression: " + name);
	}
	
	/**
	 * Logs the end of a expression evaluation.
	 * @param name the name of the expression being evaluated
	 * @param value the value of the expression
	 */
	public void endExpression(String name, Object value) {
		log("end expression: " + name + ", value: " + value);
	}
	
	/**
	 * Logs the end of a expression evaluation.
	 * @param name the name of the expression being evaluated
	 * @param value the value of the expression
	 * @param info additional information about ending the expression
	 */
	public void endExpression(String name, Object value, String info) {
		log("end expression: " + name + ", value: " + value + " (" + info + ")");
	}
	
	/**
	 * Logs an arbitrary message.
	 * @param message the message to log
	 */
	public void log(String message) {
		logger.debug(message);
	}
	
}
