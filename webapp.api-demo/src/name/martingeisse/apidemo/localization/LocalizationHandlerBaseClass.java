/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo.localization;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;

/**
 * Localization tests.
 */
public class LocalizationHandlerBaseClass implements IRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		throw new RuntimeException();
	}
	
}
