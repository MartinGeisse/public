/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.multi.populator;

import name.martingeisse.admin.single.EntityInstance;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * This populator generates a link that triggers a callback method
 * in the populator which can then act on the entity instance.
 */
public abstract class AbstractActionLinkPopulator extends AbstractEntityCellPopulator {

	/**
	 * Constructor.
	 */
	public AbstractActionLinkPopulator() {
		super(null);
	}

	/**
	 * Constructor.
	 * @param title the populator title
	 */
	public AbstractActionLinkPopulator(String title) {
		super(title);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item, java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(Item<ICellPopulator<EntityInstance>> cellItem, String componentId, IModel<EntityInstance> rowModel) {
		
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}

	/**
	 * This method is invoked when the user clicks the action link.
	 */
	protected abstract void onLinkClicked();
	
}
