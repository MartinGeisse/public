/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.database;

import com.mysema.query.sql.PostgresTemplates;
import com.mysema.query.sql.SQLTemplates;

/**
 * Concrete implementation of {@link AbstractDatabaseDescriptor} for
 * PostgreSQL databases.
 */
public class PostgresDatabaseDescriptor extends AbstractDatabaseDescriptor {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor#createSqlTemplates()
	 */
	@Override
	public SQLTemplates createSqlTemplates() {
		return new PostgresTemplates();
	}

}
