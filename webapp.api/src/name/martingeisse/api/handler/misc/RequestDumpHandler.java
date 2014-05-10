/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.misc;

import java.io.PrintWriter;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;

/**
 * This handler just dumps request information to the response.
 */
public class RequestDumpHandler implements IRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.IRequestHandler#handle(name.martingeisse.restful.request.RequestCycle, name.martingeisse.restful.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		try (PrintWriter w = requestCycle.getResponse().getWriter()) {
			w.println("Full Request path: " + requestCycle.getRequestPath().toString());
			w.println("Relative Request path: " + path.toString());
			w.flush();
		}
	}
	
}
