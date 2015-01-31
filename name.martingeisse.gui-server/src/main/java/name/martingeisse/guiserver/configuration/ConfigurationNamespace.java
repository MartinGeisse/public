/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import com.google.common.collect.ImmutableMap;

/**
 * A namespace that groups configuration elements.
 */
public final class ConfigurationNamespace implements ConfigurationElement {

	/**
	 * the elements
	 */
	private final ImmutableMap<String, ConfigurationElement> elements;

	/**
	 * the defaultPageId
	 */
	private final String defaultPageId;

	/**
	 * Constructor.
	 * 
	 * @param elements the elements of this namespace
	 * @param defaultPageId the default element ID to use for locating pages
	 */
	public ConfigurationNamespace(ImmutableMap<String, ConfigurationElement> elements, String defaultPageId) {
		this.elements = elements;
		this.defaultPageId = defaultPageId;
	}

	/**
	 * Getter method for the elements.
	 * 
	 * @return the elements
	 */
	public ImmutableMap<String, ConfigurationElement> getElements() {
		return elements;
	}

	/**
	 * Getter method for the defaultPageId.
	 * 
	 * @return the defaultPageId
	 */
	public String getDefaultPageId() {
		return defaultPageId;
	}

}
