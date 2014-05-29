/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util.zebra;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.model.IModel;

/**
 * Specialized item implementation for zebra striping.
 */
public class ZebraLoopItem extends LoopItem {

	/**
	 * Constructor.
	 * @param index the item index
	 */
	public ZebraLoopItem(int index) {
		super(index);
	}
	
	/**
	 * Constructor.
	 * @param index the item index
	 * @param model the item model
	 */
	public ZebraLoopItem(int index, IModel<?> model) {
		super(index, model);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param index the item index
	 */
	public ZebraLoopItem(String id, int index) {
		super(id, index);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param index the item index
	 * @param model the item model
	 */
	public ZebraLoopItem(String id, int index, IModel<?> model) {
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
