/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model;

import org.apache.wicket.model.IModel;

import name.martingeisse.common.datarow.DataRow;
import name.martingeisse.common.util.GenericTypeUtil;

/**
 * This model represents a single field from a {@link DataRow}.
 * @param <R> the row type
 * @param <F> the field type
 */
public class DataRowFieldModel<R extends DataRow, F> implements IModel<F> {

	/**
	 * the container
	 */
	private final Object container;
	
	/**
	 * the fieldName
	 */
	private final String fieldName;
	
	/**
	 * Constructor.
	 * @param row the row that contains the field
	 * @param fieldName the name of the field
	 */
	public DataRowFieldModel(R row, String fieldName) {
		this.container = row;
		this.fieldName = fieldName;
	}
	
	/**
	 * Constructor.
	 * @param rowModel the model for the row that contains the field
	 * @param fieldName the name of the field
	 */
	public DataRowFieldModel(IModel<R> rowModel, String fieldName) {
		this.container = rowModel;
		this.fieldName = fieldName;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
		if (container instanceof IModel<?>) {
			IModel<?> containerAsModel = (IModel<?>)container;
			containerAsModel.detach();
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	@Override
	public F getObject() {
		DataRow row;
		if (container instanceof IModel<?>) {
			IModel<?> containerAsModel = (IModel<?>)container;
			row = (DataRow)containerAsModel.getObject();
		} else {
			row = (DataRow)container;
		}
		return GenericTypeUtil.unsafeCast(row.getFieldValue(fieldName));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(F fieldValue) {
		DataRow row;
		if (container instanceof IModel<?>) {
			IModel<?> containerAsModel = (IModel<?>)container;
			row = (DataRow)containerAsModel.getObject();
		} else {
			row = (DataRow)container;
		}
		row.setFieldValue(fieldName, fieldValue);
	}

}
