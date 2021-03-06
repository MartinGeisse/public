/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template;

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
