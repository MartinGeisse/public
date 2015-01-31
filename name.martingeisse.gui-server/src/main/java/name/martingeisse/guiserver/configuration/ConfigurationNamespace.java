/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * A namespace that groups configuration elements.
 */
public final class ConfigurationNamespace extends AbstractConfigurationElement {

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

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.AbstractConfigurationElement#initializeRoot()
	 */
	@Override
	void initializeRoot() {
		super.initializeRoot();
		initializeElements();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.AbstractConfigurationElement#initialize(name.martingeisse.guiserver.configuration.ConfigurationNamespace, java.lang.String)
	 */
	@Override
	public void initialize(ConfigurationNamespace parentNamespace, String key) {
		super.initialize(parentNamespace, key);
		initializeElements();
	}

	/**
	 * 
	 */
	private void initializeElements() {
		for (Map.Entry<String, ConfigurationElement> entry : elements.entrySet()) {
			entry.getValue().initialize(this, entry.getKey());
		}
	}
	
}
