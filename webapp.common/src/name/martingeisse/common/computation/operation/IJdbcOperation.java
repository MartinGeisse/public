/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.operation;

import java.sql.Connection;

import name.martingeisse.common.jdbc.IConnectionProvider;

/**
 * An operation on a JDBC connection. Database operations typically
 * use this interface instead of {@link IPlainOperation} to re-use
 * the database connection and sometimes to share a database
 * transaction.
 * 
 * @param <T> the result type
 */
public interface IJdbcOperation<T> {

	/**
	 * Executes this operation using the specified connection.
	 * 
	 * Before any transaction handling is even considered, this action
	 * checks whether the passed value for createTransaction is
	 * supported at all, and throws an {@link UnsupportedOperationException}
	 * if that is not the case.
	 * 
	 * Otherwise, if createTransaction is false, then this method does not
	 * create a database transaction. That is, it re-uses a running transaction
	 * or, if none is running, executes without a transaction. This method
	 * should return normally if successful, and throw an exception to indicate
	 * that it demands a rollback (in case any transaction is running).
	 * 
	 * In contrast, if createTransaction is true, then this method will
	 * start its own transaction when it starts executing. The method
	 * will then perform its intended actions, and will either
	 * - commit the transaction and return its result, or
	 * - rollback the transaction and throw an exception,
	 * such that the correlation between commit/rollback and
	 * return/exception is the same, regardless of the value of
	 * createTransaction.
	 * 
	 * @param connection the connection
	 * @param createTransaction whether this method shall create its own transaction
	 * @return the result
	 */
	public T execute(Connection connection, boolean createTransaction);

	/**
	 * Executes this operation using a new connection from
	 * the specified connection provider, and disposes of the
	 * connection afterwards.
	 * 
	 * This method could in principle support a wider range of
	 * usage patterns than execute(Connection, boolean), since it
	 * need not indicate to its caller whether it demands a
	 * commit or rollback. However, for maximum consistency, this
	 * method should follow the same pattern -- that is:
	 * - on success, commit the transaction (if any) and return normally
	 * - on failure, rollback the transaction (if any) and throw an exception.
	 * 
	 * @param connectionProvider the connection provider
	 * @param useTransaction whether this method shall use a transaction
	 * @return the result
	 */
	public T execute(IConnectionProvider connectionProvider, boolean useTransaction);

}
