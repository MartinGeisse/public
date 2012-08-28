/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.application;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.handler.NamedResourceFolderHandler;
import name.martingeisse.api.handler.RequestDumpHandler;
import name.martingeisse.api.servlet.RestfulServlet;

/**
 * The main application request handler. The {@link RestfulServlet}
 * expects this class to have a no-argument constructor and to implement
 * {@link IRequestHandler}.
 */
public class ApplicationRequestHandler extends NamedResourceFolderHandler {

	/**
	 * Constructor.
	 */
	public ApplicationRequestHandler() {
		NamedResourceFolderHandler subHandler = new NamedResourceFolderHandler();
		subHandler.getResources().put("foo", new RequestDumpHandler());
		subHandler.getResources().put("bar", new RequestDumpHandler());
		getResources().put("sub", subHandler);
		getResources().put("baz", new RequestDumpHandler());
	}
	
}
