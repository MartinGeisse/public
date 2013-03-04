/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

import java.io.Serializable;
import java.util.List;

import name.martingeisse.common.terms.CommandVerb;
import name.martingeisse.wicket.javascript.IJavascriptInteractionInterceptor;

/**
 * An instance of this class represents a binding between a command
 * verb and its handler.
 *
 * @param <T> the tree node type
 * @param <P> the type of an extra parameter from an interaction interceptor
 */
final class JsTreeCommandHandlerBinding<T, P> implements Serializable {

	/**
	 * the commandVerb
	 */
	private final CommandVerb commandVerb;

	/**
	 * the handler
	 */
	private final IJsTreeCommandHandler<T, P> handler;
	
	/**
	 * the interceptor
	 */
	private final IJavascriptInteractionInterceptor<P> interceptor;

	/**
	 * Constructor.
	 * @param commandVerb the command verb
	 * @param handler the handler
	 * @param interceptor the interceptor, or null for none
	 */
	JsTreeCommandHandlerBinding(final CommandVerb commandVerb, final IJsTreeCommandHandler<T, P> handler, IJavascriptInteractionInterceptor<P> interceptor) {
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
	IJsTreeCommandHandler<T, P> getHandler() {
		return handler;
	}
	
	/**
	 * Getter method for the interceptor.
	 * @return the interceptor
	 */
	IJavascriptInteractionInterceptor<P> getInterceptor() {
		return interceptor;
	}
	
	/**
	 * This method is invoked when the client-side scripts send a command verb
	 * to the server. It looks up the appropriate command handler and invokes it.
	 * @param data the JSON-parsed extra input parameter, or null if not present in the request
	 */
	final void invoke(final List<T> selectedNodes, Object data) {
		handler.handleCommand(commandVerb, selectedNodes, interceptor == null ? null : interceptor.convertParameter(data));
	}
	
}
