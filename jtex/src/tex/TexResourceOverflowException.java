/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package tex;


/**
 * This exception gets thrown when TeX's resource are exhausted.
 */
public class TexResourceOverflowException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public TexResourceOverflowException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public TexResourceOverflowException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public TexResourceOverflowException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public TexResourceOverflowException(String message, Throwable cause) {
		super(message, cause);
	}

}
