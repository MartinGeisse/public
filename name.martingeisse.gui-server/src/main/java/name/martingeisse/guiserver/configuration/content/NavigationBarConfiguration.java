/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.content;

import com.google.common.collect.ImmutableList;

/**
 * This content element shows a navigation bar.
 */
public final class NavigationBarConfiguration implements ContentElementConfiguration {

	/**
	 * the brandLink
	 */
	private final LinkConfiguration brandLink;

	/**
	 * the elements
	 */
	private final ImmutableList<ContentElementConfiguration> elements;

	/**
	 * Constructor.
	 * @param brandLink the brand link, or null if none
	 * @param elements the navigation bar elements
	 */
	public NavigationBarConfiguration(LinkConfiguration brandLink, ImmutableList<ContentElementConfiguration> elements) {
		this.brandLink = brandLink;
		this.elements = elements;
	}

	/**
	 * Getter method for the brandLink.
	 * @return the brandLink
	 */
	public LinkConfiguration getBrandLink() {
		return brandLink;
	}

	/**
	 * Getter method for the elements.
	 * @return the elements
	 */
	public ImmutableList<ContentElementConfiguration> getElements() {
		return elements;
	}

}
