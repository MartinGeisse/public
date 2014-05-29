/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

/**
 * This exception type indicates that the request contained an
 * unknown path.
 */
public class RequestPathNotFoundException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public RequestPathNotFoundException() {
		super("path not found");
	}
	
	/**
	 * Constructor.
	 * @param path the path that was not found
	 */
	public RequestPathNotFoundException(String path) {
		super("path not found: " + path);
	}
	
	/**
	 * Constructor.
	 * @param path the path that was not found
	 */
	public RequestPathNotFoundException(RequestPathChain path) {
		super("path not found: " + path.toString());
	}
	
}
