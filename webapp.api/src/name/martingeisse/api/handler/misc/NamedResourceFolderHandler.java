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

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.handler.ISelfDescribingRequestHandler;
import name.martingeisse.api.handler.RequestHandlerUtil;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;
import name.martingeisse.api.request.RequestPathNotFoundException;

/**
 * This handler represents a "resource folder" that contains other
 * resources which are selected by name.
 * 
 * Initialization: Just add request handlers to the resource map.
 */
public class NamedResourceFolderHandler implements ISelfDescribingRequestHandler {

	/**
	 * the resources
	 */
	private final Map<String, IRequestHandler> resources;
	
	/**
	 * Constructor.
	 */
	public NamedResourceFolderHandler() {
		this.resources = new HashMap<String, IRequestHandler>();
	}
	
	/**
	 * Getter method for the resources.
	 * @return the resources
	 */
	public Map<String, IRequestHandler> getResources() {
		return resources;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.IRequestHandler#handle(name.martingeisse.restful.request.RequestCycle, name.martingeisse.restful.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		if (path.isEmpty()) {
			listContents(requestCycle);
			return;
		}
		IRequestHandler handler = resources.get(path.getHead());
		if (handler == null) {
			throw new RequestPathNotFoundException();
		}
		handler.handle(requestCycle, path.getTail());
	}
	
	/**
	 * @param requestCycle
	 */
	private void listContents(RequestCycle requestCycle) throws IOException {
		requestCycle.prepareUpgradablePlainTextResponse();
		PrintWriter w = requestCycle.getWriter();
		for (Map.Entry<String, IRequestHandler> entry : resources.entrySet()) {
			String name = entry.getKey();
			if (requestCycle.isPlainRequest()) {
				w.println(name);
			} else {
				String description = RequestHandlerUtil.getShortDescription(entry.getValue());
				w.println("<a href=\"" + name + "\">" + name + "</a> (" + description + ")<br />");
			}
		}
		requestCycle.finishTextResponse();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.ISelfDescribingRequestHandler#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return "folder";
	}

}
