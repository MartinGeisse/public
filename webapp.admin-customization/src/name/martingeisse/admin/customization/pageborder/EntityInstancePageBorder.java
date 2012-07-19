/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.pageborder;

import java.util.List;

import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.NavigationUtil;
import name.martingeisse.admin.navigation.component.NavigationMenuPanel;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * This class adds a navigation menu for entity-local navigation.
 */
public class EntityInstancePageBorder extends Border {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public EntityInstancePageBorder(String id) {
		super(id);
		addToBorder(new NavigationMenuPanel("navigationMenuPanel", new RootListModel()));
	}

	/**
	 *
	 */
	private class RootListModel extends AbstractReadOnlyModel<List<NavigationNode>> {
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
		 */
		@Override
		public List<NavigationNode> getObject() {
			NavigationNode currentNode = NavigationUtil.getCurrentNavigationNode(EntityInstancePageBorder.this);
			System.out.println("current: " + currentNode.getPath());
			return currentNode.getClosestVariableAncestor().getChildren();
		}
		
	}
	
}
