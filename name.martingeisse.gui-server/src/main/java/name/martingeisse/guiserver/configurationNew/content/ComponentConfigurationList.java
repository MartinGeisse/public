/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configurationNew.content;

import org.apache.wicket.MarkupContainer;

import com.google.common.collect.ImmutableList;

/**
 * Contains the configuration for multiple components, usually for the
 * children of a {@link MarkupContainer}.
 */
public final class ComponentConfigurationList {

	/**
	 * the configurations
	 */
	private final ImmutableList<ComponentConfiguration> configurations;

	/**
	 * Constructor.
	 * @param configurations the wrapped configurations
	 */
	public ComponentConfigurationList(ImmutableList<ComponentConfiguration> configurations) {
		this.configurations = configurations;
	}
	
	/**
	 * Getter method for the configurations.
	 * @return the configurations
	 */
	public ImmutableList<ComponentConfiguration> getConfigurations() {
		return configurations;
	}

	/**
	 * Builds all components from the configurations in this list and adds them to
	 * the specified parent.
	 * 
	 * @param parent the parent container to add the components to
	 */
	public void buildAndAddComponents(MarkupContainer parent) {
		for (ComponentConfiguration configuration : configurations) {
			parent.add(configuration.buildComponent());
		}
	}
	
}
