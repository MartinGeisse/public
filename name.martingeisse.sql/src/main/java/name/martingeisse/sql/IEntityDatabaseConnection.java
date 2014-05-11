/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.sql;

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
 * 
 * TODO: Support nested transactions:
 * - represent transactions as objects:
 *   - begin() begins a transaction and returns a T object
 *   - the T object has commit/rollback
 *   - the T has an internal "running" flag for illegal state detection
 *   - the T has commitBegin and rollbackBegin that work in-place
 *     or return a new T object (?)
 * - this interface has a method to get currently running T objects.
 * - T objects have a beginChild() that creates a child T
 * - T objects know their parent (if any) and currently running child (if any)
 * - commit on a T with running child is illegal
 * - rollback on a T with running child is valid and does a rollback on all children
 * - implementation of nested T's: savepoints.
 *   - begin sets a savepoint
 *   - commit clears the savepoint
 *   - rollback does a rollback-to-savepoint
 * - remove T management methods from this interface except for root begin and
 *   get-current.
 * - two get-current methods: get current root, get current leaf. Are these useful
 *   at all? App code should not use get-current to commit/rollback but keep the T'n
 *   object so it doesn't work on the wrong T.
 *   Correct: Keep the T object and work on that -- it's always the right one.
 *   but: missing get-current method is also confusing. best solution seems to be:
 *   include both get-current methods; warn in method comment; must be used correctly.
 *   bad solution but still the best there is.
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
