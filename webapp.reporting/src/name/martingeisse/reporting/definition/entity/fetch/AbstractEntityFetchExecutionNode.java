/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity.fetch;

import name.martingeisse.reporting.definition.entity.query.EntityQueryFetchNode;

/**
 * A node from the fully resolved fetch clause structure of an entity query.
 * Nodes of this type are generated from {@link EntityQueryFetchNode}.
 * Unlike those nodes, this class stores
 * - a one-step link instead of an entity selector
 * - a list of property names instead of property selectors.
 * 
 * Row filters are present as in the original node.
 */
public abstract class AbstractEntityFetchExecutionNode {

	/**
	 * Constructor.
	 */
	public AbstractEntityFetchExecutionNode() {
	}

}
