/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

import name.martingeisse.admin.navigation.AbstractNavigationNode;
import name.martingeisse.admin.navigation.NavigationFolder;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

/**
 * This page displays the contents of a {@link NavigationFolder} node.
 */
public class NavigationFolderPage extends AbstractAdminPage {

	/**
	 * Constructor.
	 * @param folder the navigation folder
	 */
	public NavigationFolderPage(NavigationFolder folder) {
		getMainContainer().add(new Label("folderTitle", folder.getTitle()));
		getMainContainer().add(new ListView<AbstractNavigationNode>("children", folder.getChildren()) {
			@Override
			protected void populateItem(ListItem<AbstractNavigationNode> item) {
				AbstractNavigationNode child = item.getModelObject();
				AbstractLink childLink = child.createLink("link");
				childLink.add(new Label("title", child.getTitle()));
				item.add(childLink);
			}
		});
	}
	
}