/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.populator;

import java.util.Arrays;
import java.util.List;

import name.martingeisse.admin.entity.instance.EntityInstance;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * This cell populator adds multiple sub-populators to DIVs.
 */
public class MultiCellPopulator extends AbstractEntityCellPopulator {

	/**
	 * the cellPopulators
	 */
	private final List<ICellPopulator<EntityInstance>> cellPopulators;

	/**
	 * Constructor.
	 * @param title the title
	 * @param cellPopulators the sub-populators
	 */
	public MultiCellPopulator(final String title, final ICellPopulator<EntityInstance>... cellPopulators) {
		this(title, Arrays.asList(cellPopulators));
	}

	/**
	 * Constructor.
	 * @param title the title
	 * @param cellPopulators the sub-populators
	 */
	public MultiCellPopulator(final String title, final List<ICellPopulator<EntityInstance>> cellPopulators) {
		super(title);
		this.cellPopulators = cellPopulators;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item, java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(final Item<ICellPopulator<EntityInstance>> item, final String id, final IModel<EntityInstance> instanceModel) {
		item.add(new MultiCellPanel(id, instanceModel, cellPopulators));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}

}
