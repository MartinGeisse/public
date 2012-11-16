/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.api.request;

/**
 * This exception type indicates a problem with the request.
 */
public class RequestException extends Exception {

	/**
	 * Constructor.
	 */
	public RequestException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public RequestException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public RequestException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public RequestException(String message, Throwable cause) {
		super(message, cause);
	}

}
