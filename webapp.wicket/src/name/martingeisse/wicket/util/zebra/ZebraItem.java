/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util.zebra;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Specialized item implementation for zebra striping.
 * @param <T> the model type
 */
public class ZebraItem<T> extends Item<T> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param index the item index
	 */
	public ZebraItem(String id, int index) {
		super(id, index);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param index the item index
	 * @param model the item model
	 */
	public ZebraItem(String id, int index, IModel<T> model) {
		super(id, index, model);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag) {
		super.onComponentTag(tag);
		tag.getAttributes().put("class", ((getIndex() & 1) == 0) ? "even" : "odd");
	}
	
}
