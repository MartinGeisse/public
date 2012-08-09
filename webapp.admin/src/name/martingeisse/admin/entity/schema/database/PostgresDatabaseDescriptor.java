/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.database;

import java.sql.Connection;

import com.mysema.query.sql.PostgresTemplates;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;

/**
 * Concrete implementation of {@link AbstractDatabaseDescriptor} for
 * PostgreSQL databases.
 */
public class PostgresDatabaseDescriptor extends AbstractDatabaseDescriptor {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor#createQuery(java.sql.Connection)
	 */
	@Override
	public SQLQuery createQuery(Connection connection) {
		return new SQLQueryImpl(connection, new PostgresTemplates());
	}
	
}
