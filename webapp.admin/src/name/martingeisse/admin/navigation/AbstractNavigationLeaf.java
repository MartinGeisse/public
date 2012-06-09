/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;


/**
 * The base class for all leaf nodes of the navigation tree.
 * 
 * Leaf nodes are intended to have links. Even though it is
 * formally possible for a leaf node to have no link, such
 * a node is useless (other than to indicate the absence of
 * a link, for which it may still be used). 
 */
public abstract class AbstractNavigationLeaf extends AbstractNavigationNode {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.AbstractNavigationNode#visitLeafNodes(name.martingeisse.admin.navigation.INavigationLeafVisitor)
	 */
	@Override
	public void visitLeafNodes(INavigationLeafVisitor visitor) {
		visitor.visit(this);
	}
	
}
