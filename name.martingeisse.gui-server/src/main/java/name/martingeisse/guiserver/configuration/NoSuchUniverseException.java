/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

/**
 * This exception gets thrown when a universe is needed, e.g. for request processing,
 * but none could be found.
 */
public class NoSuchUniverseException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public NoSuchUniverseException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public NoSuchUniverseException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public NoSuchUniverseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public NoSuchUniverseException(String message, Throwable cause) {
		super(message, cause);
	}

}
