/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui2;

import name.martingeisse.guiserver.configurationNew.content.LinkConfiguration;
import name.martingeisse.wicket.component.misc.InvisibleWebMarkupContainer;

import org.apache.wicket.markup.html.border.Border;

/**
 * A navigation bar. The content of this border are the elements of the navigation bar.
 */
public class NavigationBar extends Border {

	/**
	 * Constructor.
	 * 
	 * @param id the wicket id
	 * @param brandLink the configuration for the brand link, or null for none
	 */
	public NavigationBar(String id, LinkConfiguration brandLink) {
		super(id);
		if (brandLink == null) {
			addToBorder(new InvisibleWebMarkupContainer("brandLink"));
		} else {
			addToBorder(brandLink.buildComponent());
		}
	}
	
}
