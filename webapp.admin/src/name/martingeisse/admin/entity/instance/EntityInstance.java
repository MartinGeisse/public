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

/**
 * Represents a single entity instance fetched from the database. This is
 * simply a {@link DataRow} that knows the entity from which it comes.
 * 
 * The entity and the meta-data cannot be changed after construction.
 * Trying to do so results in an {@link UnsupportedOperationException}.
 */
public class EntityInstance extends DataRow {

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
	public EntityInstance(final EntityDescriptor entity, final ResultSet resultSet) throws SQLException {
		super(resultSet);
		if (entity == null) {
			throw new IllegalArgumentException("entity argument is null");
		}
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

	/**
	 * Getter method for the entityName.
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public final EntityDescriptor getEntity() {
		return entity;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.AbstractDataRowMetaHolder#setMeta(name.martingeisse.common.datarow.DataRowMeta)
	 */
	@Override
	public void setMeta(final DataRowMeta meta) {
		if (entityName != null) {
			throw new UnsupportedOperationException("cannot change meta-data after construction");
		}
		super.setMeta(meta);
	}

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public final Object getId() {
		final String idColumnName = getEntity().getIdColumnName();
		if (idColumnName == null) {
			throw new IllegalStateException("entity " + entityName + " has no primary key and cannot be viewed");
		}
		return getFieldValue(idColumnName);
	}

}
