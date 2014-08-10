/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

/**
 * This exception type indicates that the request parameters
 * could not be understood.
 */
public class ApiRequestParametersException extends ApiRequestException {

	/**
	 * Constructor.
	 * @param message the message to send as a response
	 */
	public ApiRequestParametersException(String message) {
		super(message);
	}
	
}
