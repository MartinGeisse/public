/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.datatable.populator;

import name.martingeisse.admin.entity.component.list.datatable.render.RenderingColumnDescriptor;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.EntityListFilterUtils;
import name.martingeisse.common.util.GenericTypeUtil;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.mysema.query.types.Expression;

/**
 * Customized column descriptor type for {@link PopulatorEntityDataTablePanel}.
 */
public class PopulatorColumnDescriptor extends RenderingColumnDescriptor implements ICellPopulator<EntityInstance> {

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
	public PopulatorColumnDescriptor(final String title, final ICellPopulator<EntityInstance> cellPopulator) {
		super(title);
		this.cellPopulator = cellPopulator;
	}

	/**
	 * Constructor.
	 * @param title the column title
	 * @param sortField the field used to sort this column
	 * @param cellPopulator the cell populator used to fill table cells in this column
	 */
	public PopulatorColumnDescriptor(final String title, String sortField, final ICellPopulator<EntityInstance> cellPopulator) {
		super(title, sortFieldToSortExpression(sortField));
		this.cellPopulator = cellPopulator;
	}
	
	/**
	 * Converts a field name to a QueryDSL expression of type {@link Comparable}, used for sorting.
	 * @param sortField the field name
	 * @return the expression
	 */
	private static Expression<Comparable<?>> sortFieldToSortExpression(String sortField) {
		return GenericTypeUtil.unsafeCast(EntityListFilterUtils.fieldPath(Comparable.class, sortField));
	}

	/**
	 * Constructor.
	 * @param title the column title
	 * @param sortExpression the expression used to sort rows
	 * @param cellPopulator the cell populator used to fill table cells in this column
	 */
	public PopulatorColumnDescriptor(final String title, final Expression<Comparable<?>> sortExpression, final ICellPopulator<EntityInstance> cellPopulator) {
		super(title, sortExpression);
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
	public void setCellPopulator(final ICellPopulator<EntityInstance> cellPopulator) {
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
	public void populateItem(final Item<ICellPopulator<EntityInstance>> cellItem, final String componentId, final IModel<EntityInstance> rowModel) {
		cellPopulator.populateItem(cellItem, componentId, rowModel);
	}

}
