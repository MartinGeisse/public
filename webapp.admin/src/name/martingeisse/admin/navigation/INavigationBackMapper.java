/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import org.apache.wicket.Page;


/**
 * Instances are used to map a currently visible page back to the navigation
 * tree. This shows the user his/her current location in the navigation tree.
 */
public interface INavigationBackMapper {

	/**
	 * Initializes internal variables of this mapper from the specified
	 * navigation tree. Mappers can use this to prepare data
	 * structures for faster node lookup.
	 * @param tree the navigation tree used by the application
	 */
	public void initialize(NavigationTree tree);
	
	/**
	 * Given a page, returns the navigation node to which the page belongs.
	 * Returns null if no appropriate navigation node was found.
	 * @param page the page
	 * @return the navigation node or null
	 */
	public AbstractNavigationNode mapPageToNavigationNode(Page page);
	
}
