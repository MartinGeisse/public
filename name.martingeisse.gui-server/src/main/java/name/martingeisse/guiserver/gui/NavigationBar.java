/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.wicket.component.misc.InvisibleWebMarkupContainer;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.border.Border;

/**
 * A navigation bar. The content of this border are the elements of the navigation bar.
 */
public class NavigationBar extends Border {

	/**
	 * Constructor.
	 * 
	 * @param id the wicket id
	 * @param brandLinkConfiguration the configuration for the brand link, or null for none
	 */
	public NavigationBar(String id, ComponentConfiguration brandLinkConfiguration) {
		super(id);
		if (brandLinkConfiguration == null) {
			addToBorder(new InvisibleWebMarkupContainer("brandLink"));
		} else {
			Component brandLink = brandLinkConfiguration.buildComponent();
			if (brandLink != null) {
				addToBorder(brandLink);
			}
		}
	}
	
}
