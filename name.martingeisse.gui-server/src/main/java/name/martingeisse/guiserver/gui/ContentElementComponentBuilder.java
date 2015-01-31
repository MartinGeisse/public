/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import org.apache.wicket.Component;

import name.martingeisse.guiserver.configuration.content.ContentElementConfiguration;

/**
 * This object is able to turn {@link ContentElementConfiguration} objects
 * into Wicket components.
 */
public interface ContentElementComponentBuilder {

	/**
	 * Builds a Wicket component for the specified configuration.
	 * 
	 * @param id the Wicket id
	 * @param configuration the configuration
	 * @return the component
	 */
	public Component buildComponent(String id, ContentElementConfiguration configuration);
	
}
