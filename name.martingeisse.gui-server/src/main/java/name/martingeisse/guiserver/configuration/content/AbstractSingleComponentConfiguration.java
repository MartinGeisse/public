/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.common.terms.IConsumer;

import org.apache.wicket.Component;

/**
 * Base class for component configurations that corresponding to a single Wicket component, or no component at all.
 */
public abstract class AbstractSingleComponentConfiguration extends AbstractComponentGroupConfiguration {

	/**
	 * Constructor.
	 */
	public AbstractSingleComponentConfiguration() {
	}

	/**
	 * Getter method for the component id.
	 * @return the component id
	 */
	public final String getComponentId() {
		return getComponentBaseId();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration#buildComponents(name.martingeisse.common.terms.IConsumer)
	 */
	@Override
	public void buildComponents(IConsumer<Component> consumer) {
		Component component = buildComponent();
		if (component != null) {
			consumer.consume(component);
		}
	}

	/**
	 * Builds the Wicket component for this configuration.
	 * 
	 * @return the component, or null if no component is needed
	 */
	protected abstract Component buildComponent();

}
