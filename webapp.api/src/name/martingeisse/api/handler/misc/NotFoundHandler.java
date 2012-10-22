/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.misc;

import name.martingeisse.api.handler.ISelfDescribingRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;
import name.martingeisse.api.request.RequestPathNotFoundException;
import name.martingeisse.api.servlet.ServletUtil;

/**
 * This handler throws a {@link RequestPathNotFoundException} for all requests.
 */
public class NotFoundHandler implements ISelfDescribingRequestHandler {

	/**
	 * the passToNotFoundHandler
	 */
	private boolean passToNotFoundHandler;
	
	/**
	 * Constructor.
	 * @param passToNotFoundHandler whether this handler passes the request to the
	 * "not found" handler (true) or directly responds with a 404 response (false)
	 */
	public NotFoundHandler(boolean passToNotFoundHandler) {
		this.passToNotFoundHandler = passToNotFoundHandler;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		if (passToNotFoundHandler) {
			throw new RequestPathNotFoundException(path);
		} else {
			ServletUtil.emitResourceNotFoundResponse(requestCycle.getRequest(), requestCycle.getResponse());
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.ISelfDescribingRequestHandler#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return "404";
	}
	
}
