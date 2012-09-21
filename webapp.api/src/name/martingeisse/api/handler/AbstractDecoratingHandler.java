/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler;

import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;

/**
 * Base class for the decorator pattern applied to request handlers.
 */
public abstract class AbstractDecoratingHandler implements IRequestHandler {

	/**
	 * the decoratedHandler
	 */
	private IRequestHandler decoratedHandler;

	/**
	 * Constructor.
	 * @param decoratedHandler the decorated handler
	 */
	public AbstractDecoratingHandler(IRequestHandler decoratedHandler) {
		this.decoratedHandler = decoratedHandler;
	}

	/**
	 * Getter method for the decoratedHandler.
	 * @return the decoratedHandler
	 */
	public IRequestHandler getDecoratedHandler() {
		return decoratedHandler;
	}

	/**
	 * Setter method for the decoratedHandler.
	 * @param decoratedHandler the decoratedHandler to set
	 */
	public void setDecoratedHandler(IRequestHandler decoratedHandler) {
		this.decoratedHandler = decoratedHandler;
	}

	/**
	 * Invokes the {@link #handle(RequestCycle, RequestPathChain)} of the decorated handler.
	 * @param requestCycle the request cycle
	 * @param path the remaining path
	 * @throws Exception on errors
	 */
	protected final void passToDecoratedHandler(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		decoratedHandler.handle(requestCycle, path);
	}
	
}
