/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.request;

/**
 * This exception type is thrown when parsing a request path fails.
 * This happens if the path contains a leading slash
 * or a sequence of more than one slash. A single trailing slash
 * is allowed and has no effect.
 */
public class MalformedRequestPathException extends Exception {

	/**
	 * Constructor.
	 */
	public MalformedRequestPathException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public MalformedRequestPathException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public MalformedRequestPathException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public MalformedRequestPathException(String message, Throwable cause) {
		super(message, cause);
	}

}
