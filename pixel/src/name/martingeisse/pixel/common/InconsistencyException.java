/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.common;

/**
 * This exception gets thrown when an inconsistent state
 * was detected while solving a puzzle. It is used for
 * fork-based solving.
 */
public class InconsistencyException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public InconsistencyException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public InconsistencyException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public InconsistencyException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public InconsistencyException(String message, Throwable cause) {
		super(message, cause);
	}

}
