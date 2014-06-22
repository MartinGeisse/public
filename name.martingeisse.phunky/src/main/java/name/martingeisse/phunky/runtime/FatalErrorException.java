/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

/**
 * This exception gets thrown when a PHP program triggers an unhandled
 * fatal error. It cannot be caught by a PHP catch clause.
 */
public final class FatalErrorException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public FatalErrorException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public FatalErrorException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public FatalErrorException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public FatalErrorException(String message, Throwable cause) {
		super(message, cause);
	}

}
