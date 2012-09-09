/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * This class describes a database used by the application.
 */
public abstract class AbstractDatabaseDescriptor implements IDatabaseDescriptor {

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

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.IDatabaseDescriptor#getDisplayName()
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.IDatabaseDescriptor#createJdbcConnection()
	 */
	@Override
	public Connection createJdbcConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.IDatabaseDescriptor#createConnection()
	 */
	@Override
	public JdbcEntityDatabaseConnection createConnection() {
		return new JdbcEntityDatabaseConnection(this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.IDatabaseDescriptor#createSqlTemplates()
	 */
	@Override
	public abstract SQLTemplates createSqlTemplates();

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.IDatabaseDescriptor#createQuery(java.sql.Connection)
	 */
	@Override
	public SQLQuery createQuery(final Connection connection) {
		return new SQLQueryImpl(connection, createSqlTemplates());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.IDatabaseDescriptor#createInsert(java.sql.Connection)
	 */
	@Override
	public SQLInsertClause createInsert(final Connection connection, final RelationalPath<?> entityPath) {
		return new SQLInsertClause(connection, createSqlTemplates(), entityPath);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.IDatabaseDescriptor#createUpdate(java.sql.Connection, com.mysema.query.sql.RelationalPath)
	 */
	@Override
	public SQLUpdateClause createUpdate(final Connection connection, final RelationalPath<?> entityPath) {
		return new SQLUpdateClause(connection, createSqlTemplates(), entityPath);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.IDatabaseDescriptor#createDelete(java.sql.Connection, com.mysema.query.sql.RelationalPath)
	 */
	@Override
	public SQLDeleteClause createDelete(final Connection connection, final RelationalPath<?> entityPath) {
		return new SQLDeleteClause(connection, createSqlTemplates(), entityPath);
	}

}
