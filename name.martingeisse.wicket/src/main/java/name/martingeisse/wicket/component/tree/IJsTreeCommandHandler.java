/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

import java.io.Serializable;
import java.util.List;
import name.martingeisse.common.terms.CommandVerb;

/**
 * Tree command handlers are bound to command verbs in a
 * {@link JsTree} component and invoked when the tree component
 * receives the command verb.
 *
 * @param <T> the tree node type
 * @param <P> the type of an extra parameter from an interaction interceptor
 */
public interface IJsTreeCommandHandler<T, P> extends Serializable {

	/**
	 * Handles a command verb.
	 * @param commandVerb the command verb to handle
	 * @param selectedNodes the selected tree nodes
	 * @param parameter an extra parameter from the interaction interceptor, if any
	 */
	public void handleCommand(CommandVerb commandVerb, List<T> selectedNodes, P parameter);
	
}
