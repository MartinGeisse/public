/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.pagebar;

import java.util.List;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admon.entity.AllEntityDescriptorsModel;
import name.martingeisse.admon.navigation.NavigationNode;
import name.martingeisse.admon.navigation.NavigationTree;
import name.martingeisse.admon.navigation.component.NavigationMenuView;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * The page top bar.
 */
public class BasicPageTopBar extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public BasicPageTopBar(String id) {
		super(id);
		
		// navigation
		IModel<List<NavigationNode>> topNavigationNodeListModel = new LoadableDetachableModel<List<NavigationNode>>() {
			@Override
			protected List<NavigationNode> load() {
				return NavigationTree.get().getRoot().getChildren();
			}
		};
		add(new NavigationMenuView("topNavigationNodes", topNavigationNodeListModel, 0));
	
		// entity menu
		add(new ListView<EntityDescriptor>("entities", AllEntityDescriptorsModel.instance) {
			@Override
			protected void populateItem(ListItem<EntityDescriptor> item) {
				EntityDescriptor entity = item.getModelObject();
				AbstractLink link = entity.getCanonicalListNavigationNode().createLink("link");
				link.add(new Label("name", item.getModelObject().getDisplayName()));
				item.add(link);
			}
		});
		
	}
	
}
