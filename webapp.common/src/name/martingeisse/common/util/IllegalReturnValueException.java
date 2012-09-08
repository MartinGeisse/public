/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

/**
 * This exception type indicates that a method returned
 * a value that it is not supposed to return. It is the
 * equivalent to {@link IllegalArgumentException} for
 * return values.
 */
public class IllegalReturnValueException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public IllegalReturnValueException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public IllegalReturnValueException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public IllegalReturnValueException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public IllegalReturnValueException(String message, Throwable cause) {
		super(message, cause);
	}

}
