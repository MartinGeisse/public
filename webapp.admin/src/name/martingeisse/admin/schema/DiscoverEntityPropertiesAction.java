/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.common.IAction;

/**
 * This action discovers the entity properties for a single entity.
 */
public class DiscoverEntityPropertiesAction implements IAction<List<EntityPropertyDescriptor>> {

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
	public DiscoverEntityPropertiesAction() {
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
	public List<EntityPropertyDescriptor> execute() {
		try {
			final List<EntityPropertyDescriptor> result = new ArrayList<EntityPropertyDescriptor>();
			ResultSet resultSet = connection.getMetaData().getColumns(null, null, entity.getTableName(), null);
			while (resultSet.next()) {
				final EntityPropertyDescriptor propertyDescriptor = new EntityPropertyDescriptor();
				propertyDescriptor.setName(resultSet.getString("COLUMN_NAME"));
				result.add(propertyDescriptor);
			}
			resultSet.close();
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
