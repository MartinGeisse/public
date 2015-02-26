/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.xml.ConfigurationAssemblerAcceptor;

import org.apache.wicket.Component;

/**
 * Common interface for the configuration of a group of Wicket component.
 * 
 * The common case for this interface is to contain the configuration for a
 * single component, but multiple components are possible.
 * 
 * This interface knows how to build the Wicket component. That is,
 * no complex, context-aware logic occurs when building the components.
 * All such logic occurs when building the configuration from the
 * specification, i.e. between the configuration files and the
 * configuration objects.
 */
public interface ComponentConfiguration extends ConfigurationAssemblerAcceptor<ComponentConfiguration>, IComponentConfigurationVisitorAcceptor {
	
	/**
	 * Builds the wicket component. May return null to indicate that
	 * this configuration works without a wicket component, i.e. it
	 * was just a macro for raw markup.
	 * 
	 * @return the component or null
	 */
	public Component buildComponent();
	
}
