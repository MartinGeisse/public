/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.misc;

import name.martingeisse.api.handler.IApiRequestHandler;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.request.ApiRequestPathChain;
import name.martingeisse.api.request.ApiRequestPathNotFoundException;
import name.martingeisse.api.servlet.ServletUtil;

/**
 * This handler never finds any resource. It can either throw a
 * {@link ApiRequestPathNotFoundException} or directly emit a 404 error,
 * depending on a flag specified at construction. The difference is that
 * a 404 response will be sent directly to the client, while an
 * exception can be caught by the master handler and passed to
 * a fallback handler.
 */
public class NotFoundHandler implements IApiRequestHandler {

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
	public void handle(ApiRequestCycle requestCycle, ApiRequestPathChain path) throws Exception {
		if (passToNotFoundHandler) {
			throw new ApiRequestPathNotFoundException(path);
		} else {
			ServletUtil.emitResourceNotFoundResponse(requestCycle.getRequest(), requestCycle.getResponse());
		}
	}
	
}
