/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template;

import name.martingeisse.common.terms.IConsumer;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Common interface for the configuration of a group of Wicket components.
 * 
 * The common case for this interface is to contain the configuration for a
 * single component, but multiple components are possible. (Rationale: If
 * this interface was designed to result in a single component only, then
 * groups with more than one component would have to use a dummy
 * {@link WebMarkupContainer} just to fulfil the contract).
 * 
 * This interface knows how to build the Wicket components and how to
 * generate the corresponding markup in the enclosing page or panel.
 */
public interface ComponentGroupConfiguration extends ConfigurationAssemblerAcceptor<ComponentGroupConfiguration>, IComponentGroupConfigurationVisitorAcceptor {
	
	/**
	 * Builds the wicket components for this group and gives them to the specified consumer.
	 * 
	 * @param consumer the consumer to give components to
	 */
	public void buildComponents(IConsumer<Component> consumer);

}
