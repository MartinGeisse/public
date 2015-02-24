/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.navbar;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfigurationList;

/**
 * Contains the brand link and normal link components of a {@link NavigationBarConfiguration}.
 */
public final class NavigationBarContents {

	/**
	 * the brandLink
	 */
	private final ComponentConfiguration brandLink;

	/**
	 * the links
	 */
	private final ComponentConfigurationList links;

	/**
	 * Constructor.
	 * @param brandLink the brand link, or null if none
	 * @param links the navigation links
	 */
	public NavigationBarContents(ComponentConfiguration brandLink, ComponentConfigurationList links) {
		this.brandLink = brandLink;
		this.links = links;
	}

	/**
	 * Getter method for the brandLink.
	 * @return the brandLink
	 */
	public ComponentConfiguration getBrandLink() {
		return brandLink;
	}

	/**
	 * Getter method for the links.
	 * @return the links
	 */
	public ComponentConfigurationList getLinks() {
		return links;
	}

}
