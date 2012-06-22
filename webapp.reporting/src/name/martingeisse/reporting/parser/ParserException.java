/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

/**
 * This exception is thrown when the parser cannot understand a
 * report definition document.
 */
public class ParserException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public ParserException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public ParserException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public ParserException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
