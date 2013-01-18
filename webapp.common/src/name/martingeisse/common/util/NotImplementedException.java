/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

/**
 * Thrown if a feature is not (yet) implemented.
 */
public class NotImplementedException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public NotImplementedException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public NotImplementedException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public NotImplementedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public NotImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

}
