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

/**
 * This handler throws a {@link RequestPathNotFoundException} for all requests.
 */
public class NotFoundHandler implements ISelfDescribingRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		throw new RequestPathNotFoundException(path);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.ISelfDescribingRequestHandler#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return "404";
	}
	
}
