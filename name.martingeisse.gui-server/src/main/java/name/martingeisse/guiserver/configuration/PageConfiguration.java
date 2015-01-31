/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import name.martingeisse.guiserver.configuration.content.ContentElementConfiguration;

import com.google.common.collect.ImmutableList;

/**
 * The configuration for a page.
 */
public final class PageConfiguration extends AbstractConfigurationElement {

	/**
	 * the urlPath
	 */
	private final String urlPath;
	
	/**
	 * the contentElements
	 */
	private final ImmutableList<ContentElementConfiguration> contentElements;

	/**
	 * Constructor.
	 * 
	 * @param urlPath the URL path
	 * @param contentElements the content elements
	 */
	public PageConfiguration(String urlPath, ImmutableList<ContentElementConfiguration> contentElements) {
		if (urlPath == null) {
			throw new IllegalArgumentException("urlPath argument is null");
		}
		if (contentElements == null) {
			throw new IllegalArgumentException("contentElements argument is null");
		}
		this.urlPath = urlPath;
		this.contentElements = contentElements;
	}
	
	/**
	 * Getter method for the urlPath.
	 * @return the urlPath
	 */
	public String getUrlPath() {
		return urlPath;
	}

	/**
	 * Getter method for the contentElements.
	 * @return the contentElements
	 */
	public ImmutableList<ContentElementConfiguration> getContentElements() {
		return contentElements;
	}

}
