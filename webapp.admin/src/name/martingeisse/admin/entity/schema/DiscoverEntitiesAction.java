/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.entity.property.type.IEntityIdType;
import name.martingeisse.admin.entity.property.type.ISqlType;
import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;
import name.martingeisse.common.datarow.DataRowMeta;

/**
 * This action asks the database for all stored entities.
 */
class DiscoverEntitiesAction {

	/**
	 * the database
	 */
	private AbstractDatabaseDescriptor database;

	/**
	 * Getter method for the database.
	 * @return the database
	 */
	public AbstractDatabaseDescriptor getDatabase() {
		return database;
	}

	/**
	 * Setter method for the database.
	 * @param database the database to set
	 */
	public void setDatabase(final AbstractDatabaseDescriptor database) {
		this.database = database;
	}

	/**
	 * Executes this action.
	 * @return the result
	 */
	public List<EntityDescriptor> execute() {
		Connection connection = null;
		try {
			connection = database.createConnection();
			
			// fetch all tables
			final List<EntityDescriptor> result = new ArrayList<EntityDescriptor>();
			{
				ResultSet resultSet = connection.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
				while (resultSet.next()) {
					final EntityDescriptor entityDescriptor = new EntityDescriptor();
					entityDescriptor.setDatabase(database);
					entityDescriptor.setTableName(resultSet.getString("TABLE_NAME"));
					result.add(entityDescriptor);
				}
				resultSet.close();
			}
			
			// fetch the columns for each table
			for (EntityDescriptor entityDescriptor : result) {
				DiscoverEntityPropertiesAction subAction = new DiscoverEntityPropertiesAction();
				subAction.setConnection(connection);
				subAction.setEntity(entityDescriptor);
				entityDescriptor.setProperties(subAction.execute());
			}
			
			// fetch the primary key for each table
			for (EntityDescriptor entityDescriptor : result) {
				
				// determine the ID column name by fetching database meta-data
				DiscoverEntityIdAction subAction = new DiscoverEntityIdAction();
				subAction.setConnection(connection);
				subAction.setEntity(entityDescriptor);
				String idColumnName = subAction.execute();
				
				// cannot handle tables without a primary key for now
				if (idColumnName == null) {
					continue;
				}
				
				// using the name, get column meta-data previously fetched
				EntityPropertyDescriptor idPropertyDescriptor = entityDescriptor.getProperties().get(idColumnName);
				if (idPropertyDescriptor == null) {
					throw new IllegalStateException("table meta-data of table " + entityDescriptor.getTableName() + " specified column " + idColumnName + " as its ID column, but no such column exists in the property descriptors");
				}
				ISqlType idColumnType = idPropertyDescriptor.getType();
				if (!(idColumnType instanceof IEntityIdType)) {
					throw new IllegalStateException("type of the ID column " + entityDescriptor.getTableName() + "." + idColumnName + " is not supported as an entity ID type");
				}

				// store the information in the entity descriptor
				entityDescriptor.setIdColumnName(idColumnName);
				entityDescriptor.setIdColumnType((IEntityIdType)idColumnType);
				
			}

			// Fetch the data row meta-data for each table. Unlike the properties/columns fetched
			// above this directly detects the format of the result set when fetching the entity.
			{
				Statement statement = connection.createStatement();
				for (EntityDescriptor entityDescriptor : result) {
					ResultSet resultSet = statement.executeQuery("SELECT * FROM " + entityDescriptor.getTableName() + " LIMIT 1");
					entityDescriptor.setDataRowMeta(new DataRowMeta(resultSet.getMetaData()));
					resultSet.close();
				}
			}
			
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
	}

}
