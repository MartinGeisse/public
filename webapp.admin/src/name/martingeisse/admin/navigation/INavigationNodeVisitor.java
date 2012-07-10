/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;


/**
 * Visitor interface for iterating {@link NavigationNode} instances.
 */
public interface INavigationNodeVisitor {

	/**
	 * Visits the specified node.
	 * @param node the node to visit
	 */
	public void visit(NavigationNode node);
	
}
