/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

/**
 * Visitor interface for iterating {@link AbstractNavigationLeaf} instances.
 */
public interface INavigationLeafVisitor {

	/**
	 * Visits the specified leaf.
	 * @param leaf the leaf to visit
	 */
	public void visit(AbstractNavigationLeaf leaf);
	
}
