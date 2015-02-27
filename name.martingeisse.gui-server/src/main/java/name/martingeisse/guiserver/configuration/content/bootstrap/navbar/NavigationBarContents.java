/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.bootstrap.navbar;

import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfigurationList;

/**
 * Contains the brand link and normal link components of a {@link NavigationBarConfiguration}.
 */
public final class NavigationBarContents {

	/**
	 * the brandLink
	 */
	private final ComponentGroupConfiguration brandLink;

	/**
	 * the links
	 */
	private final ComponentGroupConfigurationList links;

	/**
	 * Constructor.
	 * @param brandLink the brand link, or null if none
	 * @param links the navigation links
	 */
	public NavigationBarContents(ComponentGroupConfiguration brandLink, ComponentGroupConfigurationList links) {
		this.brandLink = brandLink;
		this.links = links;
	}

	/**
	 * Getter method for the brandLink.
	 * @return the brandLink
	 */
	public ComponentGroupConfiguration getBrandLink() {
		return brandLink;
	}

	/**
	 * Getter method for the links.
	 * @return the links
	 */
	public ComponentGroupConfigurationList getLinks() {
		return links;
	}

}
