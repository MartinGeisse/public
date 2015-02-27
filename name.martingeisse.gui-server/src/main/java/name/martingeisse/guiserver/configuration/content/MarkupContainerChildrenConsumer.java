/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.common.terms.IConsumer;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;

/**
 * Consumes components by adding them to a markup container.
 */
public final class MarkupContainerChildrenConsumer implements IConsumer<Component> {

	/**
	 * the container
	 */
	private final MarkupContainer container;

	/**
	 * Constructor.
	 * @param container the container to add components to
	 */
	public MarkupContainerChildrenConsumer(MarkupContainer container) {
		this.container = container;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.terms.IConsumer#consume(java.lang.Object)
	 */
	@Override
	public void consume(Component component) {
		container.add(component);
	}

}
