/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler;

import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.request.ApiRequestMethod;
import name.martingeisse.api.request.ApiRequestPathChain;

/**
 * Base class for handlers that expect a raw POST request. These handlers
 * should use the getBodyAs*() methods from {@link ApiRequestCycle} to obtain
 * the POST body; this allows to use a POST form parameter called "body"
 * instead. This handler base class will respond to GET requests with a
 * form that sends such a parameter; this simplifies testing the API.
 */
public abstract class AbstractRawPostHandler implements IApiRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public final void handle(ApiRequestCycle requestCycle, ApiRequestPathChain path) throws Exception {
		if (requestCycle.getRequestMethod() == ApiRequestMethod.POST) {
			handlePost(requestCycle, path);
		} else {
			requestCycle.emitBodyFormPage();
		}
	}

	/**
	 * Handles a POST request.
	 * @param requestCycle the request cycle
	 * @param path the path of the request, relative to this handler
	 * @throws Exception on errors
	 */
	protected abstract void handlePost(ApiRequestCycle requestCycle, ApiRequestPathChain path) throws Exception;
	
}
