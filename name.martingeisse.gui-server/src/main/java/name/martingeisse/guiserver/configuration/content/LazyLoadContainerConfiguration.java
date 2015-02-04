/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.wicket.component.misc.LongLoadingContainer;

import org.apache.wicket.MarkupContainer;

import com.google.common.collect.ImmutableList;

/**
 * A lazy-loading container.
 */
public final class LazyLoadContainerConfiguration extends AbstractContainerConfiguration {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param children the children
	 */
	public LazyLoadContainerConfiguration(String id, ComponentConfigurationList children) {
		super(id, children);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param children the children
	 */
	public LazyLoadContainerConfiguration(String id, ImmutableList<ComponentConfiguration> children) {
		super(id, children);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new LongLoadingContainer(getId());
	}

}
