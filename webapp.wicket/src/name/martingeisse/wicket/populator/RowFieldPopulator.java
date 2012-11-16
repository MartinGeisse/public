/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.populator;

import name.martingeisse.common.datarow.IDataRow;
import name.martingeisse.wicket.model.DataRowFieldModel;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * This populator creates a label for a field from a {@link IDataRow}.
 * TODO: Create a subclass called EntityPropertyPopulator in the
 * admin project, mostly because devs are gonna look for that and
 * not think of RowFieldPopulator... but may be useful for added features.
 * @param <T> the row type
 */
public class RowFieldPopulator<T extends IDataRow> implements ICellPopulator<T> {

	/**
	 * the fieldName
	 */
	private String fieldName;

	/**
	 * Constructor.
	 */
	public RowFieldPopulator() {
	}

	/**
	 * Constructor.
	 * @param fieldName the field name
	 */
	public RowFieldPopulator(final String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Getter method for the fieldName.
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Setter method for the fieldName.
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
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
	public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId, final IModel<T> rowModel) {
		cellItem.add(new Label(componentId, new DataRowFieldModel<T, Object>(rowModel, fieldName)));
	}

}
