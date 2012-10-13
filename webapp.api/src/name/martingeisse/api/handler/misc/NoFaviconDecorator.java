/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.misc;

import name.martingeisse.api.handler.AbstractDecoratingHandler;
import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;
import name.martingeisse.api.servlet.ServletUtil;

/**
 * This decorater is only useful for the main application handler
 * and blocks all requests to /favicon.ico with a 404 response.
 */
public class NoFaviconDecorator extends AbstractDecoratingHandler {

	/**
	 * Constructor.
	 * @param decoratedHandler the decorated handler
	 */
	public NoFaviconDecorator(IRequestHandler decoratedHandler) {
		super(decoratedHandler);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		if ("favicon.ico".equals(path.getHead())) {
			// we cannot use RequestPathNotFoundException here since that would pass the request to the old API
			ServletUtil.emitResourceNotFoundResponse(requestCycle.getRequest(), requestCycle.getResponse());
		} else {
			passToDecoratedHandler(requestCycle, path);
		}
	}

}
