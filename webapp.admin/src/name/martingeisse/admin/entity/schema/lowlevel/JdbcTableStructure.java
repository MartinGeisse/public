/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.lowlevel;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The low-level structure of a JDBC table.
 */
public final class JdbcTableStructure {

	/**
	 * the selector
	 */
	private final JdbcTableSelector selector;
	
	/**
	 * the exportedForeignKeys
	 */
	private final List<JdbcForeignKey> exportedForeignKeys;
	
	/**
	 * the importedForeignKeys
	 */
	private final List<JdbcForeignKey> importedForeignKeys;
	
	/**
	 * Constructor that takes table information from the current row of the
	 * specified result set. The result set should use the format specified for
	 * {@link DatabaseMetaData#getTables(String, String, String, String[])}.
	 * 
	 * @param resultSet the result set
	 * @throws SQLException on SQL errors
	 */
	public JdbcTableStructure(ResultSet resultSet) throws SQLException {
		this.selector = new JdbcTableSelector(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
		this.exportedForeignKeys = new ArrayList<JdbcForeignKey>();
		this.importedForeignKeys = new ArrayList<JdbcForeignKey>();
	}

	/**
	 * Getter method for the selector.
	 * @return the selector
	 */
	public JdbcTableSelector getSelector() {
		return selector;
	}

	/**
	 * Getter method for the exportedForeignKeys.
	 * @return the exportedForeignKeys
	 */
	public List<JdbcForeignKey> getExportedForeignKeys() {
		return exportedForeignKeys;
	}
	
	/**
	 * Getter method for the importedForeignKeys.
	 * @return the importedForeignKeys
	 */
	public List<JdbcForeignKey> getImportedForeignKeys() {
		return importedForeignKeys;
	}
	
}
