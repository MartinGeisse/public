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
 */
public interface IJsTreeCommandHandler<T> extends Serializable {

	/**
	 * Handles a command verb.
	 * @param commandVerb the command verb to handle
	 * @param selectedNodes the selected tree nodes
	 */
	public void handleCommand(CommandVerb commandVerb, List<T> selectedNodes);
	
}
