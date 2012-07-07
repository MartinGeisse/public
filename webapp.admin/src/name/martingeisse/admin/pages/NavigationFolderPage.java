/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

import name.martingeisse.admin.navigation.INavigationNode;
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
	 * the folder
	 */
	private final NavigationFolder folder;
	
	/**
	 * Constructor.
	 * @param folder the navigation folder
	 */
	public NavigationFolderPage(NavigationFolder folder) {
		this.folder = folder;
		getMainContainer().add(new Label("folderTitle", folder.getTitle()));
		getMainContainer().add(new ListView<INavigationNode>("children", folder.getChildren()) {
			@Override
			protected void populateItem(ListItem<INavigationNode> item) {
				INavigationNode child = item.getModelObject();
				AbstractLink childLink = child.createLink("link");
				childLink.add(new Label("title", child.getTitle()));
				item.add(childLink);
			}
		});
	}

	/**
	 * Getter method for the folder.
	 * @return the folder
	 */
	public NavigationFolder getFolder() {
		return folder;
	}
	
}
