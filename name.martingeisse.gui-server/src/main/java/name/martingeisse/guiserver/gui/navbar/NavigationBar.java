/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui.navbar;

import name.martingeisse.guiserver.configuration.content.NavigationBarConfiguration;
import name.martingeisse.guiserver.gui.ContentElementRepeater;
import name.martingeisse.guiserver.gui.link.LinkBuilder;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * A navigation bar.
 */
public class NavigationBar extends Panel {

	/**
	 * Constructor.
	 * 
	 * @param id the wicket id
	 * @param configuration the configuration
	 */
	public NavigationBar(String id, NavigationBarConfiguration configuration) {
		super(id);
		add(LinkBuilder.buildLink("brandLink", configuration.getBrandLink()));
		add(new ContentElementRepeater("elements", configuration.getElements()));
	}
	
}
