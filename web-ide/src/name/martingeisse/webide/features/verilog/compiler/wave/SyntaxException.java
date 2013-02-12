/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.compiler.wave;

/**
 * Thrown on VCD syntax errors.
 */
public class SyntaxException extends Exception {

	/**
	 * Constructor.
	 */
	public SyntaxException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public SyntaxException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public SyntaxException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public SyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

}
