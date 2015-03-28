/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.component;

import name.martingeisse.wicket.component.misc.InvisibleWebMarkupContainer;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.border.Border;

/**
 * A navigation bar. The content of this border are the elements of the navigation bar.
 */
public class NavigationBar extends Border {

	/**
	 * the BRAND_LINK_WICKET_ID
	 */
	public static final String BRAND_LINK_WICKET_ID = "brandLink";
	
	/**
	 * Constructor.
	 * 
	 * @param id the wicket id
	 * @param brandLinkConfiguration the configuration for the brand link, or null for none
	 */
	public NavigationBar(String id, Component brandLinkComponent) {
		super(id);
		addToBorder(brandLinkComponent == null ? new InvisibleWebMarkupContainer(BRAND_LINK_WICKET_ID) : brandLinkComponent);
	}
	
}
