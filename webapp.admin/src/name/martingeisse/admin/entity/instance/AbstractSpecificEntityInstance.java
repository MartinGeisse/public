/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.instance;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.orm.NonColumnGetter;
import name.martingeisse.common.datarow.DataRowMeta;

/**
 * This class provides implementations for {@link IEntityInstance}
 * methods and is intended as the base class for generated entity
 * instance classes.
 */
public abstract class AbstractSpecificEntityInstance implements IEntityInstance {

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
	@NonColumnGetter
	public final Object[] getDataRowFields() {
		return meta.getFieldValues(this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRow#getDataRowFieldValue(java.lang.String)
	 */
	@Override
	@NonColumnGetter
	public final Object getDataRowFieldValue(final String fieldName) {
		return meta.getFieldValue(this, fieldName);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRow#setDataRowFieldValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public final void setDataRowFieldValue(final String fieldName, final Object fieldValue) {
		meta.setFieldValue(this, fieldName, fieldValue);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRowMetaHolder#getDataRowMeta()
	 */
	@Override
	@NonColumnGetter
	public final DataRowMeta getDataRowMeta() {
		return meta.getDataRowMeta();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.instance.IEntityInstance#getEntityName()
	 */
	@Override
	@NonColumnGetter
	public final String getEntityName() {
		return getEntity().getName();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.instance.IEntityInstance#getEntity()
	 */
	@Override
	@NonColumnGetter
	public final EntityDescriptor getEntity() {
		return ApplicationSchema.instance.findEntityByTableName(meta.getTableName());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.instance.IEntityInstance#getEntityId()
	 */
	@Override
	@NonColumnGetter
	public final Object getEntityId() {
		return getDataRowFieldValue(getEntity().getIdColumnName());
	}

}
