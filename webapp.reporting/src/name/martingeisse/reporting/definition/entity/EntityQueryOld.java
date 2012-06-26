/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.definition.ITabularQuery;

/**
 * This query represents a high-level way to express an SQL query.
 */
public class EntityQueryOld implements ITabularQuery {

	/**
	 * the entityName
	 */
	private String entityName;

	/**
	 * Constructor.
	 */
	public EntityQueryOld() {
	}

	/**
	 * Getter method for the entityName.
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Setter method for the entityName.
	 * @param entityName the entityName to set
	 */
	public void setEntityName(final String entityName) {
		this.entityName = entityName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.ITabularQuery#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public ResultSet bindToData(DataSources dataSources) {
		try {
			String queryString = "SELECT * FROM " + entityName + " LIMIT 20";
			PreparedStatement statement = dataSources.getConnection("default").prepareStatement(queryString);
			return statement.executeQuery();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.ITabularQuery#isStatementMustBeClosed()
	 */
	@Override
	public boolean isStatementMustBeClosed() {
		return true;
	}
	
}
