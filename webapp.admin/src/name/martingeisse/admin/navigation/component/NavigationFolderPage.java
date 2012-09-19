/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.component;

import java.util.List;

import name.martingeisse.admin.component.page.AbstractAdminPage;
import name.martingeisse.admin.navigation.NavigationConfiguration;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.NavigationUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This page displays the children of a {@link NavigationNode} node.
 * It is meant as a page for nodes that have no content other
 * than their children.
 */
public class NavigationFolderPage extends AbstractAdminPage {

	/**
	 * the navigationPath
	 */
	private final String navigationPath;

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public NavigationFolderPage(final PageParameters parameters) {
		super(parameters);
		this.navigationPath = NavigationUtil.getNavigationNodeFromParameter(parameters, true).getPath();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();

		final IModel<List<NavigationNode>> topNavigationNodeListModel = new LoadableDetachableModel<List<NavigationNode>>() {
			@Override
			protected List<NavigationNode> load() {
				return NavigationConfiguration.navigationTreeParameter.get().getNodesByPath().get(navigationPath).getChildren();
			}
		};

		getMainContainer().add(new Label("folderTitle", new PropertyModel<String>(this, "folderTitle")));
		getMainContainer().add(new ListView<NavigationNode>("children", topNavigationNodeListModel) {
			@Override
			protected void populateItem(final ListItem<NavigationNode> item) {
				final NavigationNode child = item.getModelObject();
				final AbstractLink childLink = child.createLink("link");
				childLink.add(new Label("title", child.getTitle()));
				item.add(childLink);
			}
		});

	}

	/**
	 * @return the title of the folder
	 */
	public String getFolderTitle() {
		return NavigationConfiguration.navigationTreeParameter.get().getNodesByPath().get(navigationPath).getTitle();
	}

}
