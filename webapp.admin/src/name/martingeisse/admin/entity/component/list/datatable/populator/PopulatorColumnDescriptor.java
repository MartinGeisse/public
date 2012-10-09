/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.populator;

import name.martingeisse.admin.entity.component.list.datatable.render.RenderingColumnDescriptor;
import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.common.util.ParameterUtil;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.mysema.query.types.Expression;

/**
 * Customized column descriptor type for {@link PopulatorEntityDataTablePanel}.
 */
public class PopulatorColumnDescriptor extends RenderingColumnDescriptor implements ICellPopulator<IEntityInstance> {

	/**
	 * the cellPopulator
	 */
	private final ICellPopulator<IEntityInstance> cellPopulator;

	/**
	 * Constructor.
	 * @param title the column title
	 * @param cellPopulator the cell populator used to fill table cells in this column
	 */
	public PopulatorColumnDescriptor(final String title, final ICellPopulator<IEntityInstance> cellPopulator) {
		super(title);
		ParameterUtil.ensureNotNull(cellPopulator, "cellPopulator");
		this.cellPopulator = cellPopulator;
	}

	/**
	 * Constructor.
	 * @param title the column title
	 * @param sortExpression the expression used to sort rows
	 * @param cellPopulator the cell populator used to fill table cells in this column
	 */
	public PopulatorColumnDescriptor(final String title, final Expression<Comparable<?>> sortExpression, final ICellPopulator<IEntityInstance> cellPopulator) {
		super(title, sortExpression);
		ParameterUtil.ensureNotNull(cellPopulator, "cellPopulator");
		this.cellPopulator = cellPopulator;
	}

	/**
	 * Getter method for the cellPopulator.
	 * @return the cellPopulator
	 */
	public ICellPopulator<IEntityInstance> getCellPopulator() {
		return cellPopulator;
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
	public void populateItem(final Item<ICellPopulator<IEntityInstance>> cellItem, final String componentId, final IModel<IEntityInstance> rowModel) {
		cellPopulator.populateItem(cellItem, componentId, rowModel);
	}

}
