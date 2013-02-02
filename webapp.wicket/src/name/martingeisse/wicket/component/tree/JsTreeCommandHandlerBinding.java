/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

import java.io.Serializable;

import name.martingeisse.common.terms.CommandVerb;
import name.martingeisse.wicket.javascript.IJavascriptInteractionInterceptor;

/**
 * An instance of this class represents a binding between a command
 * verb and its handler.
 *
 * @param <T> the tree node type
 */
final class JsTreeCommandHandlerBinding<T> implements Serializable {

	/**
	 * the commandVerb
	 */
	private final CommandVerb commandVerb;

	/**
	 * the handler
	 */
	private final IJsTreeCommandHandler<T> handler;
	
	/**
	 * the interceptor
	 */
	private final IJavascriptInteractionInterceptor interceptor;

	/**
	 * Constructor.
	 * @param commandVerb the command verb
	 * @param handler the handler
	 * @param interceptor the interceptor, or null for none
	 */
	JsTreeCommandHandlerBinding(final CommandVerb commandVerb, final IJsTreeCommandHandler<T> handler, IJavascriptInteractionInterceptor interceptor) {
		this.commandVerb = commandVerb;
		this.handler = handler;
		this.interceptor = interceptor;
	}

	/**
	 * Getter method for the commandVerb.
	 * @return the commandVerb
	 */
	CommandVerb getCommandVerb() {
		return commandVerb;
	}
	
	/**
	 * Getter method for the handler.
	 * @return the handler
	 */
	IJsTreeCommandHandler<T> getHandler() {
		return handler;
	}
	
	/**
	 * Getter method for the interceptor.
	 * @return the interceptor
	 */
	IJavascriptInteractionInterceptor getInterceptor() {
		return interceptor;
	}
	
}
