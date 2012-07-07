/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.backmapper;

import name.martingeisse.admin.navigation.INavigationNode;
import name.martingeisse.admin.pages.NavigationFolderPage;

import org.apache.wicket.Page;

/**
 * Back-mapping for {@link NavigationFolderPage}.
 */
public class NavigationFolderPageBackMapper extends AbstractNavigationBackMapper {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.backmapper.INavigationBackMapper#mapPageToNavigationNode(org.apache.wicket.Page)
	 */
	@Override
	public INavigationNode mapPageToNavigationNode(Page page) {
		if (page instanceof NavigationFolderPage) {
			NavigationFolderPage folderPage = (NavigationFolderPage)page;
			return folderPage.getFolder();
		} else {
			return null;
		}
	}
	
}
