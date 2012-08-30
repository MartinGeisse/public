/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.populator;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * This cell populator adds multiple sub-populators to DIVs.
 * TODO: Fixed prefix texts incl. i18n
 * 
 * @param <T> the row type
 */
public class MultiCellPopulator<T> implements ICellPopulator<T> {

	/**
	 * the cellPopulators
	 */
	private final List<ICellPopulator<T>> cellPopulators;

	/**
	 * Constructor.
	 * @param cellPopulators the sub-populators
	 */
	public MultiCellPopulator(final ICellPopulator<T>... cellPopulators) {
		this(Arrays.asList(cellPopulators));
	}

	/**
	 * Constructor.
	 * @param cellPopulators the sub-populators
	 */
	public MultiCellPopulator(final List<ICellPopulator<T>> cellPopulators) {
		this.cellPopulators = cellPopulators;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item, java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(final Item<ICellPopulator<T>> item, final String id, final IModel<T> instanceModel) {
		item.add(new MultiCellPanel<T>(id, instanceModel, cellPopulators));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}

}
