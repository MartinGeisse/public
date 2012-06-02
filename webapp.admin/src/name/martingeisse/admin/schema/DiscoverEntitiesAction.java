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
 * This action asks the database for all stored entities.
 */
public class DiscoverEntitiesAction implements IAction<List<EntityDescriptor>> {

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

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.common.IAction#execute()
	 */
	@Override
	public List<EntityDescriptor> execute() {
		Connection connection = null;
		try {
			connection = database.createConnection();
			
			// fetch all tables
			final List<EntityDescriptor> result = new ArrayList<EntityDescriptor>();
			ResultSet resultSet = connection.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
			while (resultSet.next()) {
				final EntityDescriptor entityDescriptor = new EntityDescriptor();
				entityDescriptor.setDatabase(database);
				entityDescriptor.setTableName(resultSet.getString("TABLE_NAME"));
				result.add(entityDescriptor);
			}
			resultSet.close();
			
			// fetch the columns for each table
			for (EntityDescriptor entityDescriptor : result) {
				DiscoverEntityPropertiesAction subAction = new DiscoverEntityPropertiesAction();
				subAction.setConnection(connection);
				subAction.setEntity(entityDescriptor);
				entityDescriptor.setProperties(subAction.execute());
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
