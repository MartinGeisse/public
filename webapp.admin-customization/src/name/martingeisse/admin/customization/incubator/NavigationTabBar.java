/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.incubator;

import java.util.List;

import name.martingeisse.admin.navigation.component.NavigationMenuView;
import name.martingeisse.admon.navigation.NavigationNode;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Single-level navigation menu with horizontally laid out tab headers.
 */
public class NavigationTabBar extends Panel {
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public NavigationTabBar(String id, final IModel<List<NavigationNode>> model) {
		super(id, model);
	}

	/**
	 * @return the model
	 */
	@SuppressWarnings("unchecked")
	public IModel<List<NavigationNode>> getModel() {
		return (IModel<List<NavigationNode>>)getDefaultModel();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new NavigationMenuView("nodes", getModel(), 0));
	}

}
