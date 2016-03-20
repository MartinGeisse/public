/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template;



/**
 * Common base class for the content specifications for configuration elements
 * such as pages, panels, and so on.
 * 
 * This class stores wicket markup and the configuration for the components and
 * models to attach to that markup.
 */
public final class Template {

	/**
	 * the wicketMarkup
	 */
	private final String wicketMarkup;

	/**
	 * the components
	 */
	private final ComponentGroupConfigurationList components;

	/**
	 * Constructor.
	 * @param wicketMarkup the wicket markup
	 * @param components the components to attach to that markup
	 */
	public Template(String wicketMarkup, ComponentGroupConfigurationList components) {
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
	public ComponentGroupConfigurationList getComponents() {
		return components;
	}

}
