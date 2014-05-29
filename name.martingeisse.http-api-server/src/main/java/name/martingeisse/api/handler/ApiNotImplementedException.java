/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler;

import org.apache.commons.lang3.NotImplementedException;

/**
 * This exception can be thrown to indicate that an API function has not
 * been implemented yet. The main difference to a {@link NotImplementedException}
 * is that this one is "public" information about the API, and the fact that
 * the function is not implemented yet may be reported to the client.
 */
public class ApiNotImplementedException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public ApiNotImplementedException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public ApiNotImplementedException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public ApiNotImplementedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public ApiNotImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

}
