/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

import org.apache.wicket.Component;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.model.IModel;

/**
 * One instance of this component class is generated for each
 * tree node, including the roots.
 */
public class TreeItem extends AbstractItem {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public TreeItem(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public TreeItem(String id, IModel<?> model) {
		super(id, model);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#getMarkup(org.apache.wicket.Component)
	 */
	@Override
	public IMarkupFragment getMarkup(Component child) {
		return getMarkup();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#onRender()
	 */
	@Override
	protected void onRender() {
		for (Component child : this) {
			child.render();
		}
	}
	
}
