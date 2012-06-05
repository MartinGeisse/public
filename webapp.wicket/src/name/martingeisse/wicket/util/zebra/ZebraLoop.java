/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util.zebra;

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
	protected LoopItem newItem(int iteration) {
		return new ZebraLoopItem(iteration);
	}

}
