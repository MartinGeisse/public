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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The low-level structure of a JDBC table.
 */
public final class JdbcTableStructure {

	/**
	 * the selector
	 */
	private final JdbcTableSelector selector;
	
	/**
	 * the columns
	 */
	private final List<JdbcColumnStructure> columns;
	
	/**
	 * the columnsByName
	 */
	private final Map<String, JdbcColumnStructure> columnsByName;
	
	/**
	 * the primaryKeyElements
	 */
	private final List<JdbcPrimaryKeyElement> primaryKeyElements;
	
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
		this.columns = new ArrayList<JdbcColumnStructure>();
		this.columnsByName = new HashMap<String, JdbcColumnStructure>();
		this.primaryKeyElements = new ArrayList<JdbcPrimaryKeyElement>();
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
	 * Getter method for the columns.
	 * @return the columns
	 */
	public List<JdbcColumnStructure> getColumns() {
		return columns;
	}

	/**
	 * Getter method for the columnsByName.
	 * @return the columnsByName
	 */
	public Map<String, JdbcColumnStructure> getColumnsByName() {
		return columnsByName;
	}
	
	/**
	 * Getter method for the primaryKeyElements.
	 * @return the primaryKeyElements
	 */
	public List<JdbcPrimaryKeyElement> getPrimaryKeyElements() {
		return primaryKeyElements;
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

	/**
	 * Sorts the primary key elements by sequence number and initializes
	 * the columns-by-name mapping.
	 */
	public void prepare() {
		
		// initialize columns by name
		for (JdbcColumnStructure column : columns) {
			columnsByName.put(column.getSelector().getColumn(), column);
		}
		
		// sort primary key
		Collections.sort(primaryKeyElements, new Comparator<JdbcPrimaryKeyElement>() {
			@Override
			public int compare(JdbcPrimaryKeyElement x, JdbcPrimaryKeyElement y) {
				return (x.getSequenceNumber() - y.getSequenceNumber());
			}
		});
		
	}
	
}
