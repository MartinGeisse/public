/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import name.martingeisse.admin.common.IAction;

/**
 * This action discovers the ID column(s) (primary key) for a single entity.
 * Returns null if no (visible) primary key was found.
 */
class DiscoverEntityIdAction implements IAction<String> {

	/**
	 * the connection
	 */
	private Connection connection;

	/**
	 * the entity
	 */
	private EntityDescriptor entity;

	/**
	 * Constructor.
	 */
	public DiscoverEntityIdAction() {
	}

	/**
	 * Getter method for the connection.
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Setter method for the connection.
	 * @param connection the connection to set
	 */
	public void setConnection(final Connection connection) {
		this.connection = connection;
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

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.common.IAction#execute()
	 */
	@Override
	public String execute() {
		try {
			ResultSet resultSet = connection.getMetaData().getPrimaryKeys(null, null, entity.getTableName());
			try {
				if (!resultSet.next()) {
					return null;
				}
				String columnName = resultSet.getString("COLUMN_NAME");
				if (resultSet.next()) {
					// multi-column IDs not yet supported.
					return null;
				}
				resultSet.close();
				return columnName;
			} finally {
				resultSet.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
