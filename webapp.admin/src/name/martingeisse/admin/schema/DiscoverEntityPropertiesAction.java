/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.capabilities.IEntityPropertyDisplayFilter;
import name.martingeisse.admin.common.IAction;

/**
 * This action discovers the entity properties for a single entity.
 */
public class DiscoverEntityPropertiesAction implements IAction<Map<String, EntityPropertyDescriptor>> {

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
	public Map<String, EntityPropertyDescriptor> execute() {
		try {
			final Map<String, EntityPropertyDescriptor> result = new HashMap<String, EntityPropertyDescriptor>();
			ResultSet resultSet = connection.getMetaData().getColumns(null, null, entity.getTableName(), null);
			while (resultSet.next()) {
				final EntityPropertyDescriptor propertyDescriptor = new EntityPropertyDescriptor();
				propertyDescriptor.setVisible(true);
				propertyDescriptor.setName(resultSet.getString("COLUMN_NAME"));
				propertyDescriptor.setVisible(isPropertyVisible(propertyDescriptor));
				result.put(propertyDescriptor.getName(), propertyDescriptor);
			}
			resultSet.close();
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param propertyDescriptor
	 * @return
	 */
	private boolean isPropertyVisible(EntityPropertyDescriptor propertyDescriptor) {
		int score = Integer.MIN_VALUE;
		boolean visible = true;
		for (IEntityPropertyDisplayFilter filter : ApplicationConfiguration.getCapabilities().getEntityPropertyDisplayFilters()) {
			if (filter.getScore() >= score) {
				Boolean filterResult = filter.isPropertyVisible(entity, propertyDescriptor);
				if (filterResult != null) {
					score = filter.getScore();
					visible = filterResult;
				}
			}
		}
		return visible;
	}
	
}
