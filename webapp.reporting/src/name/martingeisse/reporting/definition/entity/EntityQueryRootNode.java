/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import name.martingeisse.reporting.definition.nestedtable.NestedTableRow;
import name.martingeisse.reporting.definition.nestedtable.NestedTableTable;

/**
 * The root node of an entity query.
 */
public class EntityQueryRootNode extends AbstractEntityFetchNode {

	/**
	 * the entity
	 */
	private EntityDefinition entity;

	/**
	 * Constructor.
	 */
	public EntityQueryRootNode() {
	}

	/**
	 * Constructor.
	 * @param entity the root entity to fetch
	 */
	public EntityQueryRootNode(final EntityDefinition entity) {
		this.entity = entity;
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDefinition getEntity() {
		return entity;
	}

	/**
	 * Setter method for the entity.
	 * @param entity the entity to set
	 */
	public void setEntity(final EntityDefinition entity) {
		this.entity = entity;
	}

	/**
	 * Fetches the root table.
	 * @param connection the JDBC connection to use
	 * @return the table
	 * @throws SQLException on SQL errors
	 */
	NestedTableTable fetch(Connection connection) throws SQLException{
		try {
			
			// create the table object
			NestedTableTable table = new NestedTableTable();
			table.setTitle(entity.getName());
			
			// build the query text
			String queryText = "SELECT * FROM " + entity.getDatabaseTableName();
			System.out.println("executing: " + queryText);
			
			// execute the query and add the resulting rows to the table
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(queryText);
			table.setColumnNames(EntityQueryInternalUtil.getColumnNames(resultSet));
			while (resultSet.next()) {
				NestedTableRow row = new NestedTableRow();
				row.setValues(EntityQueryInternalUtil.getRowValues(resultSet));
				table.getRows().add(row);
			}
			resultSet.close();
			statement.close();
			
			return table;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}
	
}
