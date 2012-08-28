/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.handler;

/**
 * This exception type indicates that the request parameters
 * could not be understood.
 */
public class RequestParametersException extends RuntimeException {

	/**
	 * Constructor.
	 * @param message the message to send as a response
	 */
	public RequestParametersException(String message) {
		super(message);
	}
	
}
