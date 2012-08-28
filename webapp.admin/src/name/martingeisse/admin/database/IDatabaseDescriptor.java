/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.database;

import java.sql.Connection;
import java.sql.SQLException;


import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLTemplates;

/**
 * This interface is implemented by entity databases.
 */
public interface IDatabaseDescriptor {

	/**
	 * Getter method for the displayName.
	 * @return the displayName
	 */
	public String getDisplayName();
	
	/**
	 * Creates an SQL connection to this database
	 * @return the connection
	 * @throws SQLException on SQL errors
	 */
	public Connection createJdbcConnection() throws SQLException;

	/**
	 * Creates an {@link IEntityDatabaseConnection} to this database.
	 * @return the connection
	 */
	public IEntityDatabaseConnection createConnection();

	/**
	 * Creates a QueryDSL {@link SQLTemplates} object for this database.
	 * @return the templates object
	 */
	public SQLTemplates createSqlTemplates();

	/**
	 * Creates a QueryDSL {@link SQLQuery} object for this database.
	 * @param connection the database connection
	 * @return the query
	 */
	public SQLQuery createQuery(final Connection connection);
	
}
