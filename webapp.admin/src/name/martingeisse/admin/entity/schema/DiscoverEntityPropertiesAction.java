/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.property.IRawEntityListPropertyDisplayFilter;

/**
 * This action discovers the entity properties for a single entity.
 */
class DiscoverEntityPropertiesAction {

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

	/**
	 * Executes this action.
	 * @return the result
	 */
	public Map<String, EntityPropertyDescriptor> execute() {
		try {
			final Map<String, EntityPropertyDescriptor> result = new HashMap<String, EntityPropertyDescriptor>();
			ResultSet resultSet = connection.getMetaData().getColumns(null, null, entity.getTableName(), null);
			while (resultSet.next()) {
				final EntityPropertyDescriptor propertyDescriptor = new EntityPropertyDescriptor();
				propertyDescriptor.setVisibleInRawEntityList(true);
				propertyDescriptor.setName(resultSet.getString("COLUMN_NAME"));
				propertyDescriptor.setVisibleInRawEntityList(isPropertyVisibleInRawEntityList(propertyDescriptor));
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
	private boolean isPropertyVisibleInRawEntityList(EntityPropertyDescriptor propertyDescriptor) {
		int score = Integer.MIN_VALUE;
		boolean visible = true;
		for (IRawEntityListPropertyDisplayFilter filter : EntityConfigurationUtil.getRawEntityListPropertyDisplayFilters()) {
			int filterScore = filter.getScore(entity, propertyDescriptor);
			if (filterScore >= score) {
				Boolean filterResult = filter.isPropertyVisible(entity, propertyDescriptor);
				if (filterResult != null) {
					score = filterScore;
					visible = filterResult;
				}
			}
		}
		return visible;
	}
	
}
