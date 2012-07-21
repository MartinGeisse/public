/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.instance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * This action fetches a single entity instance.
 */
public class FetchEntityInstanceAction {

	/**
	 * the entity
	 */
	private EntityDescriptor entity;

	/**
	 * the id
	 */
	private int id;

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
	 * Getter method for the id.
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter method for the id.
	 * @param id the id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Executes this action.
	 * @return the result
	 */
	public EntityInstance execute() {

		if (entity.getIdColumnName() == null) {
			throw new RuntimeException("Cannot fetch entity instance for entity " + entity.getTableName() + ": ID column unknown");
		}
		
		Connection connection = null;
		try {
			connection = entity.getDatabase().createConnection();
			final Statement statement = connection.createStatement();
			final ResultSet resultSet = entity.getDatabase().fetchRowById(statement, entity.getTableName(), entity.getIdColumnName(), id);
			final EntityInstance entityInstance = new EntityInstance(entity, resultSet);
			return (entityInstance.isEmpty() ? null : entityInstance);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (final SQLException e) {
			}
		}
	}

}
