/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class describes a database used by the application.
 * 
 * TODO: this class should not be serializable. Instead, models should be able
 * to detach and re-attach from/to instances of this class.
 */
public abstract class AbstractDatabaseDescriptor implements Serializable {

	/**
	 * the displayName
	 */
	private String displayName;

	/**
	 * the url
	 */
	private String url;

	/**
	 * the username
	 */
	private String username;

	/**
	 * the password
	 */
	private String password;

	/**
	 * Constructor.
	 */
	public AbstractDatabaseDescriptor() {
	}

	/**
	 * Getter method for the displayName.
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Setter method for the displayName.
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Getter method for the url.
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter method for the url.
	 * @param url the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	/**
	 * Getter method for the username.
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter method for the username.
	 * @param username the username to set
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * Getter method for the password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter method for the password.
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Creates an SQL connection to this database
	 * @return the connection
	 * @throws SQLException on SQL errors
	 */
	public Connection createConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	/**
	 * @return the begin character for identifier quoting
	 */
	public abstract char getIdentifierBeginQuoteCharacter();

	/**
	 * @return the end character for identifier quoting
	 */
	public abstract char getIdentifierEndQuoteCharacter();
	
	/**
	 * Returns the query text for a COUNT(*) query for the specified table.
	 * @param tableName
	 * @return
	 */
	protected String getTableSizeQuery(String tableName) {
		return "SELECT COUNT(*) FROM " + getIdentifierBeginQuoteCharacter() + tableName + getIdentifierEndQuoteCharacter();
	}
	
	/**
	 * Fetches the number of rows in a table.
	 * @param statement the JDBC statement object to use
	 * @param tableName the name of the table whose size shall be returned
	 * @return the number of rows in the table
	 * @throws SQLException on SQL errors
	 */
	public int fetchTableSize(Statement statement, String tableName) throws SQLException {
		final ResultSet resultSet = statement.executeQuery(getTableSizeQuery(tableName));
		if (!resultSet.next()) {
			throw new RuntimeException("unexpected result set layout for COUNT(*) (no row)");
		}
		if (resultSet.getMetaData().getColumnCount() != 1) {
			throw new RuntimeException("unexpected result set layout for COUNT(*) (number of columns is " + resultSet.getMetaData().getColumnCount() + ")");
		}
		int count = resultSet.getInt(1);
		if (resultSet.next()) {
			throw new RuntimeException("unexpected result set layout for COUNT(*) (more than one row)");
		}
		resultSet.close();
		return count;
	}

	/**
	 * Returns the default ORDER BY clause for this database. This clause is needed to keep
	 * the order of a query stable if repeatedly executed. May return an empty string if such
	 * a clause is not necessary. Keeping the order stable is most important with
	 * pagination.
	 * 
	 * For example, MySQL does not need an ORDER BY and returns an empty string. In contrast,
	 * PostgreSQL does need an ORDER BY because otherwise subsequent queries are not guaranteed
	 * to keep the order stable. Row updates would then change the row order and totally
	 * confuse a user who is viewing the table in a paginated way.
	 * 
	 * @return the default ORDER BY clause
	 */
	public abstract String getDefaultOrderClause();
	
	/**
	 * Fetches a row from the specified table by its ID. This fetches at most one row (using LIMIT),
	 * but may return no rows at all if the ID was not found.
	 * 
	 * @param statement the JDBC statement object to use
	 * @param tableName the name of the table from which to fetch
	 * @param idColumnName the name of the ID column
	 * @param id the ID of the row to fetch
	 * @return the result set
	 * @throws SQLException on SQL errors
	 */
	public ResultSet fetchRowById(Statement statement, String tableName, String idColumnName, Object id) throws SQLException {
		char b = getIdentifierBeginQuoteCharacter();
		char e = getIdentifierEndQuoteCharacter();
		// TODO can currently only handle numeric IDs
		return statement.executeQuery("SELECT * FROM " + b + tableName + e + " WHERE " + b + idColumnName + e + " = " + id + " LIMIT 1");
	}
	
}
