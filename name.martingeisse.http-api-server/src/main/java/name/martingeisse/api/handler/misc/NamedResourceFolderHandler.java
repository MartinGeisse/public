/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.misc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.api.handler.IApiRequestHandler;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.request.ApiRequestPathChain;
import name.martingeisse.api.request.ApiRequestPathNotFoundException;

/**
 * This handler represents a "resource folder" that contains other
 * resources which are selected by name.
 * 
 * Initialization: Just add request handlers to the resource map.
 */
public class NamedResourceFolderHandler implements IApiRequestHandler {

	/**
	 * the resources
	 */
	private final Map<String, IApiRequestHandler> resources;
	
	/**
	 * Constructor.
	 */
	public NamedResourceFolderHandler() {
		this.resources = new HashMap<String, IApiRequestHandler>();
	}
	
	/**
	 * Getter method for the resources.
	 * @return the resources
	 */
	public Map<String, IApiRequestHandler> getResources() {
		return resources;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.IRequestHandler#handle(name.martingeisse.restful.request.RequestCycle, name.martingeisse.restful.request.RequestPathChain)
	 */
	@Override
	public void handle(ApiRequestCycle requestCycle, ApiRequestPathChain path) throws Exception {
		if (path.isEmpty()) {
			listContents(requestCycle);
			return;
		}
		IApiRequestHandler handler = resources.get(path.getHead());
		if (handler == null) {
			throw new ApiRequestPathNotFoundException();
		}
		handler.handle(requestCycle, path.getTail());
	}
	
	/**
	 * @param requestCycle
	 */
	private void listContents(ApiRequestCycle requestCycle) throws IOException {
		requestCycle.preparePlainTextResponse();
		PrintWriter w = requestCycle.getWriter();
		for (Map.Entry<String, IApiRequestHandler> entry : resources.entrySet()) {
			String name = entry.getKey();
			w.println(name);
		}
		requestCycle.finishTextResponse();
	}

}
