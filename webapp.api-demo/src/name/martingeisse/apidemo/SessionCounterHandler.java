/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;
import name.martingeisse.api.request.SessionKey;

/**
 * Increments a per-session counter.
 */
public class SessionCounterHandler implements IRequestHandler {

	/**
	 * the sessionKey
	 */
	private static final SessionKey<Integer> sessionKey = new SessionKey<Integer>();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		
		// increment the counter
		Integer value = sessionKey.get(requestCycle);
		if (value == null) {
			value = 0;
		}
		value++;
		sessionKey.set(requestCycle, value);
		
		// generate the response
		requestCycle.preparePlainTextResponse();
		requestCycle.getWriter().println(value);
		
	}
	
}
