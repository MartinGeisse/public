/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import org.apache.wicket.MarkupContainer;

import com.google.common.collect.ImmutableList;

/**
 * Contains the configuration for multiple component groups, usually for the
 * children of a {@link MarkupContainer}.
 */
public final class ComponentGroupConfigurationList implements IComponentGroupConfigurationVisitorAcceptor {

	/**
	 * the configurations
	 */
	private final ImmutableList<ComponentGroupConfiguration> configurations;

	/**
	 * Constructor.
	 * @param configurations the wrapped configurations
	 */
	public ComponentGroupConfigurationList(ImmutableList<ComponentGroupConfiguration> configurations) {
		this.configurations = configurations;
	}
	
	/**
	 * Getter method for the configurations.
	 * @return the configurations
	 */
	public ImmutableList<ComponentGroupConfiguration> getConfigurations() {
		return configurations;
	}

	/**
	 * Builds all components from the configurations in this list and adds them to
	 * the specified parent.
	 * 
	 * @param parent the parent container to add the components to
	 */
	public void buildAndAddComponents(MarkupContainer parent) {
		for (ComponentGroupConfiguration configuration : configurations) {
			configuration.buildComponents(new MarkupContainerChildrenConsumer(parent));
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.IComponentConfigurationVisitorAcceptor#accept(name.martingeisse.guiserver.configuration.content.IComponentConfigurationVisitor)
	 */
	@Override
	public void accept(IComponentGroupConfigurationVisitor visitor) {
		for (ComponentGroupConfiguration configuration : configurations) {
			configuration.accept(visitor);
		}
	}
	
}
