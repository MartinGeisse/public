/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.populator;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * A wicket panel that renders multiple cells to DIVs
 * using {@link ICellPopulator}s.
 * 
 * Among other uses, this can be used as the
 * component added by a parent cell populator that groups
 * multiple sub-populators in a multi-cell.
 * @param <T> the row type
 */
public class MultiCellPanel<T> extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the row model
	 * @param cellPopulators the sub-populators
	 */
	public MultiCellPanel(final String id, final IModel<T> model, final List<ICellPopulator<T>> cellPopulators) {
		super(id, model);
		add(new ListView<ICellPopulator<T>>("subcells", cellPopulators) {

			/* (non-Javadoc)
			 * @see org.apache.wicket.markup.html.list.ListView#newItem(int, org.apache.wicket.model.IModel)
			 */
			@Override
			protected ListItem<ICellPopulator<T>> newItem(final int index, final IModel<ICellPopulator<T>> itemModel) {
				return new Item<ICellPopulator<T>>(Integer.toString(index), index, itemModel);
			}

			@Override
			protected void populateItem(final ListItem<ICellPopulator<T>> item) {
				item.getModelObject().populateItem((Item<ICellPopulator<T>>)item, "subcell", MultiCellPanel.this.getModel());
			}

		});
	}

	/**
	 * @return the model for the entity instance
	 */
	@SuppressWarnings("unchecked")
	public IModel<T> getModel() {
		return (IModel<T>)getDefaultModel();
	}

}
