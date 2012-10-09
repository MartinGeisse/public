/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.instance;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.datarow.DataRow;
import name.martingeisse.common.datarow.DataRowMeta;
import name.martingeisse.common.util.ParameterUtil;

/**
 * Raw implementation of {@link IEntityInstance} based on a data array.
 * This is simply a {@link DataRow} that knows the entity from which it comes.
 * 
 * The entity and the meta-data cannot be changed after construction.
 * Trying to do so results in an {@link UnsupportedOperationException}.
 */
public class RawEntityInstance extends DataRow implements IEntityInstance {

	/**
	 * the entityName -- stored to make the entity itself detachable (transient)
	 */
	private final String entityName;

	/**
	 * the entity
	 */
	private transient EntityDescriptor entity;

	/**
	 * Constructor that takes the current row from the
	 * result set. This constructor does not advance the
	 * result set.
	 * 
	 * @param entity the entity
	 * @param resultSet the result set
	 * @throws SQLException on SQL errors
	 */
	public RawEntityInstance(final EntityDescriptor entity, final ResultSet resultSet) throws SQLException {
		super(ParameterUtil.ensureNotNull(resultSet, "resultSet"), ParameterUtil.ensureNotNull(entity, "entity").getDataRowTypes(), entity.getDatabase());
		entity.checkDataRowMeta(this);
		this.entityName = entity.getName();
		this.entity = entity;
	}

	/**
	 * Serialization support.
	 */
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.entity = ApplicationSchema.instance.findOptionalEntity(entityName);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.instance.IEntityInstance#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return entityName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.instance.IEntityInstance#getEntity()
	 */
	@Override
	public final EntityDescriptor getEntity() {
		return entity;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.AbstractDataRowMetaHolder#setMeta(name.martingeisse.common.datarow.DataRowMeta)
	 */
	@Override
	public void setDataRowMeta(final DataRowMeta meta) {
		if (entityName != null) {
			throw new UnsupportedOperationException("cannot change meta-data after construction");
		}
		super.setDataRowMeta(meta);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.instance.IEntityInstance#getEntityId()
	 */
	@Override
	public final Object getEntityId() {
		final String idColumnName = getEntity().getIdColumnName();
		if (idColumnName == null) {
			throw new IllegalStateException("entity " + entityName + " has no primary key and cannot be viewed");
		}
		return getDataRowFieldValue(idColumnName);
	}

}
