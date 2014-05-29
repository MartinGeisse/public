/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.sql;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.SQLTemplates;

/**
 * Concrete implementation of {@link AbstractDatabaseDescriptor} for
 * MySQL databases.
 */
public class MysqlDatabaseDescriptor extends AbstractDatabaseDescriptor {

	/**
	 * Constructor.
	 */
	public MysqlDatabaseDescriptor() {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor#createSqlTemplates()
	 */
	@Override
	public SQLTemplates createSqlTemplates() {
		return new MySQLTemplates(true);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.IDatabaseDescriptor#getDateTimePattern()
	 */
	@Override
	public String getDateTimePattern() {
		return "yyyy-MM-dd HH:mm:ss.S";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.IDatabaseDescriptor#getDatePattern()
	 */
	@Override
	public String getDatePattern() {
		return "yyyy-MM-dd";
	}
	
}
