/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * This is an abstraction of connections to entity databases.
 * Instances are provided by the connection manager and released
 * automatically by the corresponding servlet filter at the end
 * of each HTTP request.
 * 
 * Note that this interfaces uses an SQL-like transaction API style
 * (begin/commit/rollback), unlike JDBC's "autocommit" style.
 */
public interface IEntityDatabaseConnection {

	/**
	 * Checks whether a transaction is running. NOTE: This method can
	 * only detect transactions that are properly initiated through
	 * methods of this class. If client code directly sends
	 * transaction control statements to the database, this method
	 * won't know about it!
	 * 
	 * @return true if a transaction is running, false if not
	 */
	public boolean isTransactionRunning();
	
	/**
	 * Begins a transaction.
	 */
	public void begin();
	
	/**
	 * Commits the current transaction.
	 */
	public void commit();
	
	/**
	 * Commits the current transaction and immediately starts another one.
	 */
	public void commitBegin();
	
	/**
	 * Rolls back the current transaction.
	 */
	public void rollback();
	
	/**
	 * Rolls back the current transaction and immediately starts another one.
	 */
	public void rollbackBegin();
	
	/**
	 * Disposes of this connection. This method is called by the connection manager
	 * and should not be used by code which just wants to use the connection.
	 */
	public void dispose();
	
	/**
	 * Creates a new query for this database.
	 * @return the query
	 */
	public SQLQuery createQuery();

	/**
	 * Creates a new insert clause for this database.
	 * @param entityPath the entity to create an insert clause for
	 * @return the insert clause
	 */
	public SQLInsertClause createInsert(RelationalPath<?> entityPath);

	/**
	 * Creates a new update clause for this database.
	 * @param entityPath the entity to create an update clause for
	 * @return the update clause
	 */
	public SQLUpdateClause createUpdate(RelationalPath<?> entityPath);

	/**
	 * Creates a new delete clause for this database.
	 * @param entityPath the entity to create an delete clause for
	 * @return the delete clause
	 */
	public SQLDeleteClause createDelete(RelationalPath<?> entityPath);
	
}
