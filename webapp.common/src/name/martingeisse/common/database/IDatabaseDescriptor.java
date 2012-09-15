/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;

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

	/**
	 * Creates a QueryDSL {@link SQLInsertClause} object for this database.
	 * @param connection the database connection
	 * @param entityPath the entity to create an insert clause for
	 * @return the insert clause
	 */
	public SQLInsertClause createInsert(final Connection connection, RelationalPath<?> entityPath);

	/**
	 * Creates a QueryDSL {@link SQLUpdateClause} object for this database.
	 * @param connection the database connection
	 * @param entityPath the entity to create an update clause for
	 * @return the update clause
	 */
	public SQLUpdateClause createUpdate(final Connection connection, RelationalPath<?> entityPath);

	/**
	 * Creates a QueryDSL {@link SQLDeleteClause} object for this database.
	 * @param connection the database connection
	 * @param entityPath the entity to create an delete clause for
	 * @return the delete clause
	 */
	public SQLDeleteClause createDelete(final Connection connection, RelationalPath<?> entityPath);
	
	/**
	 * Returns the time zone that is used by default -- that is, if no other time zone is
	 * explicitly specified -- to convert the local date-time values from the database to
	 * instants in time and back. This time zone may be missing (null) to use local values
	 * instead of instants in the application. 
	 * 
	 * Some background: The database stores local date-time values, that is, values without
	 * a time zone. Such values are not directly related to instants in time. Databases
	 * typically also store a server-wide time zone; this is unreliable though since there
	 * is little guarantee that the time zone setting is correct. See below for the
	 * consequences if this setting is indeed correct. 
	 * 
	 * If the application wants to treat these values as the local values they are, then
	 * no further handling is needed; set this default time zone to null to receive
	 * local values from queries.
	 * 
	 * If the application wants to treat these values as instants by applying them to
	 * some implied time zone, then it should set this time zone as the default time
	 * zone to receive instants from queries.
	 * 
	 * Note that the time zone settings from both the SQL server and the JDBC connection are
	 * ignored and/or circumvented by the Admin framework. If, for example, the SQL server
	 * setting is indeed correct, then the application should simply set this time zone
	 * as the default time zone and work with instants.
	 * 
	 * Note also that this default time zone only affects conversion of date-time
	 * values. Date values are always returned as local values -- Joda {@link LocalDate}
	 * to be precise -- since even in combination with a time zone do they not represent
	 * an instant in the first place. 
	 * 
	 * TODO: The application should be able to override the time zone (including override
	 * with null) for any entity or entity property.
	 * 
	 * @return the default time zone to receive instants from queries, or null to
	 * receive local date/time values.
	 */
	public DateTimeZone getDefaultTimeZone();
	
	/**
	 * Returns the database-specific Joda pattern to parse a date value that is
	 * returned as a string from the database. This value is specific to the database
	 * vendor and not typically set by application code.
	 * 
	 * @return the Joda pattern for dates from the database
	 */
	public String getDatePattern();
	
	/**
	 * Returns the database-specific Joda pattern to parse a date-time value that is
	 * returned as a string from the database. This value is specific to the database
	 * vendor and not typically set by application code.
	 * 
	 * @return the Joda pattern for date-times from the database
	 */
	public String getDateTimePattern();
	
}
