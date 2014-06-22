/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

/**
 * This exception gets thrown by a PHP program that wants to
 * exit. It cannot be caught by a PHP catch clause.
 */
public final class ExitException extends RuntimeException {

	/**
	 * the statusCode
	 */
	private final int statusCode;

	/**
	 * Constructor.
	 * @param statusCode the exit status code
	 */
	public ExitException(int statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * Getter method for the statusCode.
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	
}
