/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.instance;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.datarow.DataRowMeta;

/**
 * This class provides implementations for {@link IEntityInstance}
 * methods and is intended as the base class for generated entity
 * instance classes.
 * 
 * TODO: make abstract
 * TODO: implement
 */
public class AbstractSpecificEntityInstance implements IEntityInstance {

	/**
	 * the meta
	 */
	private final SpecificEntityInstanceMeta meta;
	
	/**
	 * Constructor.
	 * @param meta the meta-data for the concrete subclass
	 */
	public AbstractSpecificEntityInstance(final SpecificEntityInstanceMeta meta) {
		this.meta = meta;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRow#getDataRowFields()
	 */
	@Override
	public Object[] getDataRowFields() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRow#getDataRowFieldValue(java.lang.String)
	 */
	@Override
	public Object getDataRowFieldValue(String fieldName) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRow#setDataRowFieldValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setDataRowFieldValue(String fieldName, Object fieldValue) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRowMetaHolder#getDataRowMeta()
	 */
	@Override
	public DataRowMeta getDataRowMeta() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.instance.IEntityInstance#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.instance.IEntityInstance#getEntity()
	 */
	@Override
	public EntityDescriptor getEntity() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.instance.IEntityInstance#getEntityId()
	 */
	@Override
	public Object getEntityId() {
		return null;
	}

	
}
