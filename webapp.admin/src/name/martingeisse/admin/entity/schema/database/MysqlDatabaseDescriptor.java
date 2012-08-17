/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.database;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.SQLTemplates;

/**
 * Concrete implementation of {@link AbstractDatabaseDescriptor} for
 * MySQL databases.
 */
public class MysqlDatabaseDescriptor extends AbstractDatabaseDescriptor {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor#createSqlTemplates()
	 */
	@Override
	public SQLTemplates createSqlTemplates() {
		return new MySQLTemplates();
	}

}
