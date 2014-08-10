/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.misc;

import java.io.PrintWriter;

import name.martingeisse.api.handler.IApiRequestHandler;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.request.ApiRequestPathChain;

/**
 * This handler just dumps request information to the response.
 */
public class RequestDumpHandler implements IApiRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.IRequestHandler#handle(name.martingeisse.restful.request.RequestCycle, name.martingeisse.restful.request.RequestPathChain)
	 */
	@Override
	public void handle(ApiRequestCycle requestCycle, ApiRequestPathChain path) throws Exception {
		try (PrintWriter w = requestCycle.getResponse().getWriter()) {
			w.println("Full Request path: " + requestCycle.getRequestPath().toString());
			w.println("Relative Request path: " + path.toString());
			w.flush();
		}
	}
	
}
