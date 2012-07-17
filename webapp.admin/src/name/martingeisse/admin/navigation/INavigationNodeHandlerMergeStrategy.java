/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

/**
 * Implementations know how to merge two {@link INavigationNodeHandler}s.
 * 
 * Note that merging two navigation subtrees involes merging handlers as well as
 * merging child nodes. Since the two are usually specified independently --
 * that is, a node's children have nothing to do with a node's handler --
 * this class is only concerned with the handler, not with the children.
 */
public interface INavigationNodeHandlerMergeStrategy {

	/**
	 * Merges the specified node handlers.
	 * @param firstHandler the first node handler
	 * @param secondHandler the second node handler
	 * @return the merged node handler
	 */
	public INavigationNodeHandler merge(INavigationNodeHandler firstHandler, INavigationNodeHandler secondHandler);
	
}
