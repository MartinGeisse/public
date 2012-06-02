/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.model.IModel;

/**
 * A {@link Loop} that supports "Zebra Striping" by setting the
 * CSS classes "even" / "odd" on its items. CSS styles themselves
 * are not set by this class.
 */
public abstract class ZebraLoop extends Loop {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param iterations the number of iterations
	 */
	public ZebraLoop(String id, int iterations) {
		super(id, iterations);
		
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model for the number of iterations
	 */
	public ZebraLoop(String id, IModel<Integer> model) {
		super(id, model);
		
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.list.Loop#newItem(int)
	 */
	@Override
	protected LoopItem newItem(final int iteration) {
		return new LoopItem(iteration) {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.getAttributes().put("class", ((iteration & 1) == 0) ? "even" : "odd");
			}
		};
	}

}
