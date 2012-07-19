/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.pageborder;

import java.util.List;

import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.component.NavigationMenuView;
import name.martingeisse.admin.pages.EntityTablePage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * The page border.
 */
public class BasicPageBorder extends Border {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public BasicPageBorder(String id) {
		super(id);
		
		// navigation
		IModel<List<NavigationNode>> topNavigationNodeListModel = new LoadableDetachableModel<List<NavigationNode>>() {
			@Override
			protected List<NavigationNode> load() {
				return NavigationConfigurationUtil.getNavigationTree().getRoot().getChildren();
			}
		};
		addToBorder(new NavigationMenuView("topNavigationNodes", topNavigationNodeListModel, 0));
	
		// entity menu
		addToBorder(new ListView<EntityDescriptor>("entities", ApplicationSchema.instance.getEntityDescriptors()) {
			@Override
			protected void populateItem(ListItem<EntityDescriptor> item) {
				PageParameters parameters = new PageParameters();
				parameters.add("entity", item.getModelObject().getTableName());
				BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", EntityTablePage.class, parameters);
				link.add(new Label("name", EntityConfigurationUtil.getGeneralEntityConfiguration().getEntityName(item.getModelObject()))); // TODO display name
				item.add(link);
			}
		});
		
	}
	
}
