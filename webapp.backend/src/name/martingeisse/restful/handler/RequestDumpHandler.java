/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.handler;

import java.io.PrintWriter;

import name.martingeisse.restful.request.RequestCycle;
import name.martingeisse.restful.request.RequestPathChain;

/**
 * This handler just dumps request information to the response.
 */
public class RequestDumpHandler implements IRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.IRequestHandler#handle(name.martingeisse.restful.request.RequestCycle, name.martingeisse.restful.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		PrintWriter w = requestCycle.getResponse().getWriter();
		w.println("Full Request path: " + RequestPathChain.toString(requestCycle.getRequestPath()));
		w.println("Relative Request path: " + RequestPathChain.toString(path));
		w.flush();
		w.close();
	}

}
