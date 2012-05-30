/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.single;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import name.martingeisse.admin.schema.EntityDescriptor;

/**
 * Represents a single entity instance fetched from the database. This class
 * is used by entity presenters. It is not used by table views since they
 * use a more compact representation.
 * 
 * This class can also be used to signal *absence* of an entity. In this
 * class, the fields are null and the isEmpty() method returns true. This
 * happens, for example, when an instance is created from a {@link ResultSet}
 * without rows.
 */
public class EntityInstance {

	/**
	 * the entity
	 */
	private EntityDescriptor entity;

	/**
	 * the fieldNames
	 */
	private String[] fieldNames;

	/**
	 * the fieldValues
	 */
	private Object[] fieldValues;

	/**
	 * Constructor.
	 */
	public EntityInstance() {
	}

	/**
	 * Constructor.
	 * @param entity the entity
	 * @param fieldNames the field names
	 * @param fieldValues the field values
	 */
	public EntityInstance(final EntityDescriptor entity, final String[] fieldNames, final Object[] fieldValues) {
		this.entity = entity;
		this.fieldNames = fieldNames;
		this.fieldValues = fieldValues;
	}

	/**
	 * Constructor. This constructor fetches a single row from the
	 * result set. If no rows are left, this instance is initialized
	 * as empty.
	 * @param entity the entity
	 * @param resultSet the result set
	 * @throws SQLException on SQL errors
	 */
	public EntityInstance(final EntityDescriptor entity, final ResultSet resultSet) throws SQLException {
		this.entity = entity;
		if (resultSet.next()) {
			final ResultSetMetaData metaData = resultSet.getMetaData();
			final int width = metaData.getColumnCount();
			fieldNames = new String[width];
			fieldValues = new Object[width];
			for (int i = 0; i < width; i++) {
				fieldNames[i] = metaData.getColumnLabel(1 + i);
				fieldValues[i] = resultSet.getObject(1 + i);
			}
		}
	}

	/**
	 * Checks if this instance is empty. This is the case if either
	 * the field names or values are null.
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return (fieldNames == null || fieldValues == null);
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}

	/**
	 * Setter method for the entity.
	 * @param entity the entity to set
	 */
	public void setEntity(final EntityDescriptor entity) {
		this.entity = entity;
	}

	/**
	 * Getter method for the fieldNames.
	 * @return the fieldNames
	 */
	public String[] getFieldNames() {
		return fieldNames;
	}

	/**
	 * Setter method for the fieldNames.
	 * @param fieldNames the fieldNames to set
	 */
	public void setFieldNames(final String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	/**
	 * Getter method for the fieldValues.
	 * @return the fieldValues
	 */
	public Object[] getFieldValues() {
		return fieldValues;
	}

	/**
	 * Setter method for the fieldValues.
	 * @param fieldValues the fieldValues to set
	 */
	public void setFieldValues(final Object[] fieldValues) {
		this.fieldValues = fieldValues;
	}

}