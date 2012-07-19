/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import name.martingeisse.admin.navigation.component.NavigationMenuPanel;

import org.apache.wicket.markup.html.border.Border;

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
		addToBorder(NavigationMenuPanel.createForLocalNavigation("navigationMenuPanel", Integer.MAX_VALUE));
	}
	
}
