/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler;

/**
 * Implemented by request handlers that are able to return a description
 * of themselves. Useful for "upgraded" user-interface handling.
 */
public interface ISelfDescribingRequestHandler extends IRequestHandler {

	/**
	 * @return a short (one-line) description of this handler
	 */
	public String getShortDescription();
	
}
