/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler;

import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;

/**
 * Requests are prepared into {@link RequestCycle} instances and passed to
 * handlers which implement this interface. The application provides a
 * main handler that takes all requests and does the dispatch.
 * 
 * Note that handler instances are typically used by multiple threads
 * at the same time. Access to any shared data must be synchronized.
 * A typical way to deal with request handlers is to configure them
 * at startup and not modify them later.
 */
public interface IRequestHandler {

	/**
	 * Handles a request.
	 * @param requestCycle the request cycle
	 * @param path the path of the request, relative to this handler
	 * @throws Exception on errors
	 */
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception;
	
}
