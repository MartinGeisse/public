/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.misc;

import java.io.PrintWriter;

import name.martingeisse.api.handler.ISelfDescribingRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;

/**
 * This handler just dumps request information to the response.
 */
public class RequestDumpHandler implements ISelfDescribingRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.IRequestHandler#handle(name.martingeisse.restful.request.RequestCycle, name.martingeisse.restful.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		PrintWriter w = requestCycle.getResponse().getWriter();
		w.println("Full Request path: " + requestCycle.getRequestPath().toString());
		w.println("Relative Request path: " + path.toString());
		w.println("is plain request: " + requestCycle.isPlainRequest());
		w.flush();
		w.close();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.ISelfDescribingRequestHandler#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return "request dump";
	}
	
}
