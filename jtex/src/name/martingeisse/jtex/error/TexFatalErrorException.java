/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.jtex.error;

/**
 * This exception type is thrown on fatal errors during TeX processing.
 */
public class TexFatalErrorException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public TexFatalErrorException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public TexFatalErrorException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public TexFatalErrorException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public TexFatalErrorException(String message, Throwable cause) {
		super(message, cause);
	}

}
