/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition;

import java.sql.ResultSet;
import java.sql.SQLException;

import name.martingeisse.reporting.datasource.DataSources;

/**
 * This class returns tabular data by executing an SQL query.
 */
public class SqlQuery implements ITabularQuery {

	/**
	 * the dataSourceId
	 */
	private String dataSourceId;

	/**
	 * the query
	 */
	private String query;

	/**
	 * Constructor.
	 */
	public SqlQuery() {
	}

	/**
	 * Constructor.
	 * @param dataSourceId the ID of the data source to use for the query
	 * @param query the query to execute
	 */
	public SqlQuery(final String dataSourceId, final String query) {
		this.dataSourceId = dataSourceId;
		this.query = query;
	}

	/**
	 * Getter method for the dataSourceId.
	 * @return the dataSourceId
	 */
	public String getDataSourceId() {
		return dataSourceId;
	}

	/**
	 * Setter method for the dataSourceId.
	 * @param dataSourceId the dataSourceId to set
	 */
	public void setDataSourceId(final String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	/**
	 * Getter method for the query.
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Setter method for the query.
	 * @param query the query to set
	 */
	public void setQuery(final String query) {
		this.query = query;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.ITabularQuery#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public ResultSet bindToData(DataSources dataSources) {
		try {
			return dataSources.getStatement(dataSourceId).executeQuery(query);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.ITabularQuery#isStatementMustBeClosed()
	 */
	@Override
	public boolean isStatementMustBeClosed() {
		return false;
	}
	
}
