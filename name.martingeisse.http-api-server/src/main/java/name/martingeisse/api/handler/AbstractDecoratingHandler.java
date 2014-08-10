/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler;

import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.request.ApiRequestPathChain;

/**
 * Base class for the decorator pattern applied to request handlers.
 */
public abstract class AbstractDecoratingHandler implements IApiRequestHandler {

	/**
	 * the decoratedHandler
	 */
	private IApiRequestHandler decoratedHandler;

	/**
	 * Constructor.
	 * @param decoratedHandler the decorated handler
	 */
	public AbstractDecoratingHandler(IApiRequestHandler decoratedHandler) {
		this.decoratedHandler = decoratedHandler;
	}

	/**
	 * Getter method for the decoratedHandler.
	 * @return the decoratedHandler
	 */
	public IApiRequestHandler getDecoratedHandler() {
		return decoratedHandler;
	}

	/**
	 * Setter method for the decoratedHandler.
	 * @param decoratedHandler the decoratedHandler to set
	 */
	public void setDecoratedHandler(IApiRequestHandler decoratedHandler) {
		this.decoratedHandler = decoratedHandler;
	}

	/**
	 * Invokes the {@link #handle(ApiRequestCycle, ApiRequestPathChain)} of the decorated handler.
	 * @param requestCycle the request cycle
	 * @param path the remaining path
	 * @throws Exception on errors
	 */
	protected final void passToDecoratedHandler(ApiRequestCycle requestCycle, ApiRequestPathChain path) throws Exception {
		decoratedHandler.handle(requestCycle, path);
	}
	
}
