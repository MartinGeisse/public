/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A node from the fetch clause structure of an entity query. A node can be
 * - the root node that fetches the root entity
 * - a bind node that joins another entity based on a defined link and optional filters
 * - a join node that joins another entity based on fully customized conditions
 * 
 * A node may also contain property sets, row filters, and similar options.
 */
public abstract class AbstractEntityFetchNode {

	/**
	 * the children
	 */
	private List<AbstractEntityFetchNode> children = new ArrayList<AbstractEntityFetchNode>();

	/**
	 * Constructor.
	 */
	public AbstractEntityFetchNode() {
	}

	/**
	 * Getter method for the children.
	 * @return the children
	 */
	public List<AbstractEntityFetchNode> getChildren() {
		return children;
	}

	/**
	 * Setter method for the children.
	 * @param children the children to set
	 */
	public void setChildren(final List<AbstractEntityFetchNode> children) {
		this.children = children;
	}

}
