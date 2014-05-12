/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util.zebra;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * A {@link DataView} that supports "Zebra Striping" by setting the
 * CSS classes "even" / "odd" on its items. CSS styles themselves
 * are not set by this class.
 * @param <T> the model type
 */
public abstract class ZebraDataView<T> extends DataView<T> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param dataProvider the data provider
	 */
	public ZebraDataView(final String id, final IDataProvider<T> dataProvider) {
		super(id, dataProvider);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param dataProvider the data provider
	 * @param itemsPerPage the number of items per page
	 */
	public ZebraDataView(final String id, final IDataProvider<T> dataProvider, final int itemsPerPage) {
		super(id, dataProvider, itemsPerPage);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.RefreshingView#newItem(java.lang.String, int, org.apache.wicket.model.IModel)
	 */
	@Override
	protected Item<T> newItem(String id, int index, IModel<T> model) {
		return new ZebraItem<T>(id, index, model);
	}
	
}
