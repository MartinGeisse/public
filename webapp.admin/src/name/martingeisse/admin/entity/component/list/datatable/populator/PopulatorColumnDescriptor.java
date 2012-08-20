/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.populator;

import name.martingeisse.admin.entity.component.list.datatable.DataTableColumnDescriptor;
import name.martingeisse.admin.entity.instance.EntityInstance;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Customized column descriptor type for {@link PopulatorEntityDataTablePanel}.
 */
public class PopulatorColumnDescriptor extends DataTableColumnDescriptor implements ICellPopulator<EntityInstance> {

	/**
	 * the cellPopulator
	 */
	private ICellPopulator<EntityInstance> cellPopulator;
	
	/**
	 * Constructor.
	 */
	public PopulatorColumnDescriptor() {
	}

	/**
	 * Constructor.
	 * @param title the column title
	 * @param cellPopulator the cell populator used to fill table cells in this column
	 */
	public PopulatorColumnDescriptor(String title, ICellPopulator<EntityInstance> cellPopulator) {
		super(title);
		this.cellPopulator = cellPopulator;
	}

	/**
	 * Getter method for the cellPopulator.
	 * @return the cellPopulator
	 */
	public ICellPopulator<EntityInstance> getCellPopulator() {
		return cellPopulator;
	}

	/**
	 * Setter method for the cellPopulator.
	 * @param cellPopulator the cellPopulator to set
	 */
	public void setCellPopulator(ICellPopulator<EntityInstance> cellPopulator) {
		this.cellPopulator = cellPopulator;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item, java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(Item<ICellPopulator<EntityInstance>> cellItem, String componentId, IModel<EntityInstance> rowModel) {
		cellPopulator.populateItem(cellItem, componentId, rowModel);
	}

}
