/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler;

import java.io.PrintWriter;

import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;
import name.martingeisse.common.javascript.JavascriptAssembler;

/**
 * Base class for handlers that use a {@link JavascriptAssembler} to output
 * Javascript or, more commonly, JSON.
 */
public abstract class AbstractJsonOutputHandler implements IRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.IRequestHandler#handle(name.martingeisse.restful.request.RequestCycle, name.martingeisse.restful.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		JavascriptAssembler assembler = new JavascriptAssembler();
		handle(requestCycle, path, assembler);
		PrintWriter w = requestCycle.getResponse().getWriter();
		w.println(assembler.getAssembledCode());
		w.flush();
		w.close();
	}
	
	/**
	 * Handles the request.
	 * @param requestCycle the request cycle
	 * @param path the local path
	 * @param assembler the Javascript assembler
	 * @throws Exception on errors
	 */
	protected abstract void handle(RequestCycle requestCycle, RequestPathChain path, JavascriptAssembler assembler) throws Exception;

}
