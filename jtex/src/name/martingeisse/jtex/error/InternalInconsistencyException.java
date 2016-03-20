/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex.error;

/**
 * This exception type is thrown when internal variables are in an inconsistent
 * state that "should not occur".
 */
public class InternalInconsistencyException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public InternalInconsistencyException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public InternalInconsistencyException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public InternalInconsistencyException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public InternalInconsistencyException(String message, Throwable cause) {
		super(message, cause);
	}

}
