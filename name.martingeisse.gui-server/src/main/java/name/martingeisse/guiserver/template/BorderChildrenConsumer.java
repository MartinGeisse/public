/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template;

import name.martingeisse.common.terms.IConsumer;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.border.Border;

/**
 * Consumes components by adding them to a border using the special
 * {@link Border#addToBorder(Component...)} method.
 */
public final class BorderChildrenConsumer implements IConsumer<Component> {

	/**
	 * the border
	 */
	private final Border border;

	/**
	 * Constructor.
	 * @param border the border to add components to
	 */
	public BorderChildrenConsumer(Border border) {
		this.border = border;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.terms.IConsumer#consume(java.lang.Object)
	 */
	@Override
	public void consume(Component component) {
		border.addToBorder(component);
	}

}
