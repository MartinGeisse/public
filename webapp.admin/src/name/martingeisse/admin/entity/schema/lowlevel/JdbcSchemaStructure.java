/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.lowlevel;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The low-level structure of a JDBC database.
 */
public final class JdbcSchemaStructure implements ILowlevelDatabaseStructure {

	/**
	 * the tables
	 */
	private final List<JdbcTableStructure> tables;
	
	/**
	 * the tablesByName
	 */
	private final Map<String, JdbcTableStructure> tablesByName;
	
	/**
	 * the foreignKeys
	 */
	private final List<JdbcForeignKey> foreignKeys;
	
	/**
	 * Constructor.
	 * @param connection the JDBC connection
	 * @throws SQLException on SQL errors
	 */
	public JdbcSchemaStructure(Connection connection) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		
		// detect all tables
		this.tables = new ArrayList<JdbcTableStructure>();
		this.tablesByName = new HashMap<String, JdbcTableStructure>();
		ResultSet tablesResulSet = metaData.getTables(null, null, null, new String[] {"TABLE"});
		while (tablesResulSet.next()) {
			JdbcTableStructure table = new JdbcTableStructure(tablesResulSet);
			tables.add(table);
			tablesByName.put(table.getSelector().getTable(), table);
		}
		tablesResulSet.close();
		tablesResulSet = null;
		
		// detect all columns (NOTE: they are already sorted by JDBC)
		ResultSet columnsResultSet = connection.getMetaData().getColumns(null, null, null, null);
		while (columnsResultSet.next()) {
			JdbcColumnStructure column = new JdbcColumnStructure(columnsResultSet);
			JdbcColumnSelector columnSelector = column.getSelector();
			JdbcTableSelector columnTableSelector = columnSelector.copyTableSelector();
			JdbcTableStructure table = tablesByName.get(columnSelector.getTable());
			if (table == null) {
				throw new RuntimeException("found no table for column: " + columnSelector);
			}
			JdbcTableSelector tableSelector = table.getSelector();
			if (!tableSelector.equals(columnTableSelector)) {
				throw new RuntimeException("found wrong table " + tableSelector + " for column: " + columnSelector + ", expected " + columnTableSelector);
			}
			table.getColumns().add(column);
		}
		columnsResultSet.close();
		columnsResultSet = null;
		
		// detect all primary keys
		for (JdbcTableStructure table : tables) {
			JdbcTableSelector tableSelector = table.getSelector();
			ResultSet primaryKeyResultSet = connection.getMetaData().getPrimaryKeys(null, null, tableSelector.getTable());
			while (primaryKeyResultSet.next()) {
				JdbcPrimaryKeyElement element = new JdbcPrimaryKeyElement(primaryKeyResultSet);
				JdbcTableSelector elementTableSelector = element.getSelector().copyTableSelector();
				if (!elementTableSelector.equals(tableSelector)) {
					throw new RuntimeException("found primary key element " + element.getSelector() + " for wrong table " + tableSelector + ", belongs to " + elementTableSelector);
				}
				table.getPrimaryKeyElements().add(element);
			}
			primaryKeyResultSet.close();
			primaryKeyResultSet = null;
		}
		
		// prepare tables
		for (JdbcTableStructure table : tables) {
			table.prepare();
		}
		
		// detect all foreign keys (first step: raw detection)
		this.foreignKeys = new ArrayList<JdbcForeignKey>();
		for (JdbcTableStructure parentTable : tables) {
			Map<String, JdbcForeignKey> foreignKeysForThisTable = null;
			JdbcTableSelector parentSelector = parentTable.getSelector();
			ResultSet exportedKeysResultSet = metaData.getExportedKeys(parentSelector.getCatalog(), parentSelector.getSchema(), parentSelector.getTable());
			while (exportedKeysResultSet.next()) {
				if (foreignKeysForThisTable == null) {
					foreignKeysForThisTable = new HashMap<String, JdbcForeignKey>();
				}
				JdbcForeignKeyElement element = new JdbcForeignKeyElement(exportedKeysResultSet);
				String foreignKeyName = element.getForeignKeyName();
				JdbcForeignKey foreignKey = foreignKeysForThisTable.get(foreignKeyName);
				if (foreignKey == null) {
					foreignKey = new JdbcForeignKey(foreignKeyName);
					foreignKeysForThisTable.put(foreignKeyName, foreignKey);
					foreignKeys.add(foreignKey);
				}
				foreignKey.getElements().add(element);
			}
			exportedKeysResultSet.close();
			exportedKeysResultSet = null;
			foreignKeysForThisTable = null;
		}
		
		// detect all foreign keys (second step: sort the elements and store the keys in the table descriptors)
		for (JdbcForeignKey foreignKey : foreignKeys) {
			foreignKey.prepare();
			tablesByName.get(foreignKey.getParentTable().getTable()).getExportedForeignKeys().add(foreignKey);
			tablesByName.get(foreignKey.getForeignTable().getTable()).getImportedForeignKeys().add(foreignKey);
		}
		
	}
	
	/**
	 * Getter method for the tables.
	 * @return the tables
	 */
	public List<JdbcTableStructure> getTables() {
		return tables;
	}
	
	/**
	 * Getter method for the tablesByName.
	 * @return the tablesByName
	 */
	public Map<String, JdbcTableStructure> getTablesByName() {
		return tablesByName;
	}
	
	/**
	 * Getter method for the foreignKeys.
	 * @return the foreignKeys
	 */
	public List<JdbcForeignKey> getForeignKeys() {
		return foreignKeys;
	}

	/**
	 * Dumps this object to System.out
	 */
	public void dump() {
		for (Map.Entry<String, JdbcTableStructure> tableEntry : tablesByName.entrySet()) {
			JdbcTableStructure table = tableEntry.getValue();
			System.out.println();
			System.out.println("Table " + tableEntry.getKey() + " - " + table.getSelector());
			dump(table.getExportedForeignKeys(), "exported");
			dump(table.getImportedForeignKeys(), "imported");
		}
	}
	
	/**
	 * 
	 */
	private void dump(List<JdbcForeignKey> foreignKeys, String prefix) {
		for (JdbcForeignKey foreignKey : foreignKeys) {
			System.out.println("  " + prefix + " key (" + foreignKey.getParentTable() + " <-> " + foreignKey.getForeignTable() + ")");
			for (JdbcForeignKeyElement element : foreignKey.getElements()) {
				System.out.println("    " + element.getElementSequenceNumber() + ": " + element.getParentColumn() + " <-> " + element.getForeignColumn());
			}
		}
	}
	
}
