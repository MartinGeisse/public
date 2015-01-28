/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.util;


/**
 * This exception type gets thrown to signal an error message
 * that should be displayed to the user.
 */
public class UserVisibleMessageException extends RuntimeException {

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public UserVisibleMessageException(String message) {
		super(message);
	}

}
