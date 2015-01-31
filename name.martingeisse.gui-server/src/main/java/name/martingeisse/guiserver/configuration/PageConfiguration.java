/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import name.martingeisse.guiserver.configuration.content.ContentElementConfiguration;

import com.google.common.collect.ImmutableList;

/**
 * The configuration for a page.
 */
public final class PageConfiguration implements ConfigurationElement {

	/**
	 * the contentElements
	 */
	private final ImmutableList<ContentElementConfiguration> contentElements;

	/**
	 * Constructor.
	 * @param contentElements the content elements
	 */
	public PageConfiguration(ImmutableList<ContentElementConfiguration> contentElements) {
		this.contentElements = contentElements;
	}

	/**
	 * Getter method for the contentElements.
	 * @return the contentElements
	 */
	public ImmutableList<ContentElementConfiguration> getContentElements() {
		return contentElements;
	}

}
