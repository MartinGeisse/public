/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo.localization;

import name.martingeisse.api.i18n.LocalizationUtil;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;

/**
 * Localization tests.
 */
public class LocalizationHandler extends LocalizationHandlerBaseClass {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		String key = (path == null ? "default" : path.getUri().substring(1));
		requestCycle.preparePlainTextResponse();
		requestCycle.emitMessageResponse(200, LocalizationUtil.localize(LocalizationHandler.class, key, 23.42));
	}
	
}
