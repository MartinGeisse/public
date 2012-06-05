/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util.zebra;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * A {@link DataGridView} that supports "Zebra Striping" by setting the
 * CSS classes "even" / "odd" on its items. CSS styles themselves
 * are not set by this class.
 * @param <T> the model type
 */
public class ZebraDataGridView<T> extends DataGridView<T> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param populators the cell populators
	 * @param dataProvider the data provider
	 */
	public ZebraDataGridView(String id, List<? extends ICellPopulator<T>> populators, IDataProvider<T> dataProvider) {
		super(id, populators, dataProvider);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.AbstractDataGridView#newRowItem(java.lang.String, int, org.apache.wicket.model.IModel)
	 */
	@Override
	protected Item<T> newRowItem(String id, int index, IModel<T> model) {
		return new ZebraItem<T>(id, index, model);
	}
	
}
