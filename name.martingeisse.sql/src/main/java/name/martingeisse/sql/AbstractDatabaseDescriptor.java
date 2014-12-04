/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.sql;

import java.sql.Connection;
import java.sql.SQLException;
import name.martingeisse.sql.config.CustomMysqlQuerydslConfiguration;
import org.joda.time.DateTimeZone;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.mysema.query.sql.Configuration;
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
	 * the defaultTimeZone
	 */
	private DateTimeZone defaultTimeZone;
	
	/**
	 * the connectionPool
	 */
	private BoneCP connectionPool;

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

	/**
	 * Getter method for the defaultTimeZone.
	 * @return the defaultTimeZone
	 */
	@Override
	public DateTimeZone getDefaultTimeZone() {
		return defaultTimeZone;
	}

	/**
	 * Setter method for the defaultTimeZone.
	 * @param defaultTimeZone the defaultTimeZone to set
	 */
	public void setDefaultTimeZone(final DateTimeZone defaultTimeZone) {
		this.defaultTimeZone = defaultTimeZone;
	}

	/**
	 * Initializes this database descriptor.
	 */
	public void initialize() {
		try {
			connectionPool = createConnectionPool();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates the connection pool. This method is invoked by
	 * {@link #initialize()}.
	 */
	protected BoneCP createConnectionPool() throws SQLException {
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(url);
		config.setUsername(username); 
		config.setPassword(password);
		config.setMinConnectionsPerPartition(20); // won't go greater than the mininum for some reason
		config.setMaxConnectionsPerPartition(20);
		config.setPartitionCount(1);
		config.setDisableJMX(true);
		return new BoneCP(config);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.IDatabaseDescriptor#createJdbcConnection()
	 */
	@Override
	public Connection createJdbcConnection() throws SQLException {
		return connectionPool.getConnection();
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

	/**
	 * Creates a QueryDSL {@link Configuration}. This configuration uses the SQL templates returned
	 * by {@link #createSqlTemplates()}. It also uses custom handlers to convert temporal values
	 * to Joda-time values (QueryDSL itself uses JDBC temporal support, which is horribly buggy).
	 * @return the configuration
	 */
	public Configuration createQuerydslConfiguration() {
		return new CustomMysqlQuerydslConfiguration(createSqlTemplates(), defaultTimeZone);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.database.IDatabaseDescriptor#createQuery(java.sql.Connection)
	 */
	@Override
	public SQLQuery createQuery(final Connection connection) {
		return new SQLQueryImpl(connection, createQuerydslConfiguration());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.IDatabaseDescriptor#createInsert(java.sql.Connection)
	 */
	@Override
	public SQLInsertClause createInsert(final Connection connection, final RelationalPath<?> entityPath) {
		return new SQLInsertClause(connection, createQuerydslConfiguration(), entityPath) {
			
		};
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.IDatabaseDescriptor#createUpdate(java.sql.Connection, com.mysema.query.sql.RelationalPath)
	 */
	@Override
	public SQLUpdateClause createUpdate(final Connection connection, final RelationalPath<?> entityPath) {
		return new SQLUpdateClause(connection, createQuerydslConfiguration(), entityPath);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.database.IDatabaseDescriptor#createDelete(java.sql.Connection, com.mysema.query.sql.RelationalPath)
	 */
	@Override
	public SQLDeleteClause createDelete(final Connection connection, final RelationalPath<?> entityPath) {
		return new SQLDeleteClause(connection, createQuerydslConfiguration(), entityPath);
	}

}
