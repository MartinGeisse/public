/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

/**
 * This exception type indicates a problem with the request. The message
 * of this exception will be sent to the client.
 */
public class ApiRequestException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public ApiRequestException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public ApiRequestException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public ApiRequestException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public ApiRequestException(String message, Throwable cause) {
		super(message, cause);
	}

}
