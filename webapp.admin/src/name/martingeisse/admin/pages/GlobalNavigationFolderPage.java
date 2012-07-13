/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationMountedRequestMapper;
import name.martingeisse.admin.navigation.NavigationNode;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This page displays the children of a {@link NavigationNode} node.
 * It is meant as a page for nodes that have no content other
 * than their children.
 */
public class GlobalNavigationFolderPage extends AbstractAdminPage {

	/**
	 * the folder
	 */
	private final NavigationNode folder;

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public GlobalNavigationFolderPage(PageParameters parameters) {
		super(parameters);
		this.folder = NavigationMountedRequestMapper.getCurrentNode(NavigationConfigurationUtil.getGlobalNavigationTree(), parameters, true);
		initialize();
	}

	/**
	 * Constructor.
	 * @param folder the navigation folder
	 */
	public GlobalNavigationFolderPage(NavigationNode folder) {
		this.folder = folder;
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		getMainContainer().add(new Label("folderTitle", folder.getTitle()));
		getMainContainer().add(new ListView<NavigationNode>("children", folder.getChildren()) {
			@Override
			protected void populateItem(ListItem<NavigationNode> item) {
				NavigationNode child = item.getModelObject();
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
	public NavigationNode getFolder() {
		return folder;
	}

}
