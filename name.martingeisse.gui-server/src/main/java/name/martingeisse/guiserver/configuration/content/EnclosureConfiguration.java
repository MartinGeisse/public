/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.gui.FirstChildEnclosureContainer;

import org.apache.wicket.MarkupContainer;

import com.google.common.collect.ImmutableList;

/**
 * Configuration for a (wicket:enclosure)-like container.
 */
public final class EnclosureConfiguration extends AbstractContainerConfiguration {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param children the children
	 */
	public EnclosureConfiguration(String id, ComponentConfigurationList children) {
		super(id, children);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param children the children
	 */
	public EnclosureConfiguration(String id, ImmutableList<ComponentConfiguration> children) {
		super(id, children);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new FirstChildEnclosureContainer(getId());
	}

}
