/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.schema;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class describes a database used by the application.
 * 
 * TODO: this class should not be serializable. Instead, models should be able
 * to detach and re-attach from/to instances of this class.
 */
public class DatabaseDescriptor implements Serializable {

	/**
	 * the displayName
	 */
	private String displayName;

	/**
	 * Constructor.
	 */
	public DatabaseDescriptor() {
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
	 * Creates an SQL connection to this database
	 * @return the connection
	 * @throws SQLException on SQL errors
	 */
	public Connection createConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:postgresql://localhost/leckerMittag", "postgres", "postgres");
	}
	
}
