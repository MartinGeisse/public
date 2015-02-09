/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import org.apache.wicket.Component;

/**
 * Common interface for the configuration of a Wicket component.
 * 
 * This interface knows how to build the Wicket component. That is,
 * no complex, context-aware logic occurs when building the components.
 * All such logic occurs when building the configuration from the
 * specification, i.e. between the configuration files and the
 * configuration objects.
 */
public interface ComponentConfiguration extends IComponentConfigurationVisitorAcceptor {
	
	/**
	 * Builds the wicket component.
	 * 
	 * @return the component
	 */
	public Component buildComponent();
	
}
