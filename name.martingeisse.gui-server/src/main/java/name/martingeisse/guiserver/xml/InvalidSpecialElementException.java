/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

/**
 * This exception indicates an unknown special element, or a special
 * element that occurs in a place where it should not be.
 */
public final class InvalidSpecialElementException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public InvalidSpecialElementException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public InvalidSpecialElementException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public InvalidSpecialElementException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public InvalidSpecialElementException(String message, Throwable cause) {
		super(message, cause);
	}

}