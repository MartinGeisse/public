/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.elements;

import name.martingeisse.guiserver.configuration.content.ComponentConfigurationList;


/**
 * Common base class for the content specifications for configuration elements
 * such as pages, panels, and so on.
 * 
 * This class stores wicket markup and the configuration for the components and
 * models to attach to that markup.
 */
public final class ConfigurationElementContent {

	/**
	 * the wicketMarkup
	 */
	private final String wicketMarkup;

	/**
	 * the components
	 */
	private final ComponentConfigurationList components;

	/**
	 * Constructor.
	 * @param wicketMarkup the wicket markup
	 * @param components the components to attach to that markup
	 */
	public ConfigurationElementContent(String wicketMarkup, ComponentConfigurationList components) {
		this.wicketMarkup = wicketMarkup;
		this.components = components;
	}

	/**
	 * Getter method for the wicketMarkup.
	 * @return the wicketMarkup
	 */
	public String getWicketMarkup() {
		return wicketMarkup;
	}

	/**
	 * Getter method for the components.
	 * @return the components
	 */
	public ComponentConfigurationList getComponents() {
		return components;
	}

}
