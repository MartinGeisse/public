/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

/**
 * This exception is thrown to indicate that request handling was
 * finished somewhere within (nested) request handlers. Handlers
 * should pass this exception. The servlet catches it and finishes
 * normally.
 * 
 * This exception is typically used in error cases, right after
 * writing a message about the error to the response. However,
 * it may be used in non-error cases as well. Handlers throwing
 * this exception in normal cases must document to do so, since
 * outer handlers might intend to wrap the response of an inner
 * handler, and would not properly finish the wrapping response
 * data in case of an exception. 
 */
public class ApiRequestHandlingFinishedException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public ApiRequestHandlingFinishedException() {
	}

}
