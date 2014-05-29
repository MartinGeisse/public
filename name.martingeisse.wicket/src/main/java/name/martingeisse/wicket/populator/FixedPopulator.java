/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.populator;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Adds a label with a fixed text.
 * @param <T> the row type
 */
public class FixedPopulator<T> implements ICellPopulator<T> {

	/**
	 * the text
	 */
	private String text;

	/**
	 * Constructor.
	 */
	public FixedPopulator() {
	}

	/**
	 * Constructor.
	 * @param text the text for the label
	 */
	public FixedPopulator(final String text) {
		super();
		this.text = text;
	}

	/**
	 * Getter method for the text.
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter method for the text.
	 * @param text the text to set
	 */
	public void setText(final String text) {
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item, java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
		cellItem.add(new Label(componentId, text));
	}

}
