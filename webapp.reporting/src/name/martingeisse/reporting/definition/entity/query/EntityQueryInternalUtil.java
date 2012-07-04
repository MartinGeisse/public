/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import name.martingeisse.reporting.definition.nestedtable.NestedTableRow;
import name.martingeisse.reporting.definition.nestedtable.NestedTableTable;

/**
 * Utility methods for the implementation of entity queries.
 */
final class EntityQueryInternalUtil {

	/**
	 * @param connection
	 * @param tableName
	 * @return
	 */
	static NestedTableTable fetchRootTable(Connection connection, String tableName) {
		try {
			
			// create the table object
			NestedTableTable table = new NestedTableTable();
			table.setTitle(tableName);
			
			// build the query text
			String queryText = "SELECT * FROM " + tableName;
			System.out.println("executing: " + queryText);
			
			// execute the query and add the resulting rows to the table
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(queryText);
			table.setColumnNames(getColumnNames(resultSet));
			while (resultSet.next()) {
				NestedTableRow row = new NestedTableRow();
				row.setValues(getRowValues(resultSet));
				table.getRows().add(row);
			}
			resultSet.close();
			statement.close();
			
			return table;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}
	
	/**
	 * Fetches a set of "similar" subtables, i.e. one subtable for each row of a set of
	 * similar tables. Similar tables have the same structure and can be fetched in a single
	 * SQL query; they behave much like a single table except that the rows are associated
	 * with different parent tables (and possibly repeated for multiple parent tables).
	 * 
	 * This method fetches all relevant child rows using key equality: The key column from
	 * the subtables specified by childTableKeyName must be equal to any parent key (see
	 * below) for a child row to be included, and such a child row is associated with any
	 * parent which it matches. Note that for consistency a child table is created for all
	 * parent rows, leaving the child table empty if no child row matches.
	 * 
	 * The set of parent keys is determined by taking all values of the column specified
	 * by parentTableKeyName from all rows of all parent tables.
	 * 
	 * The list of child tables created is also returned.
	 * 
	 * @param connection the SQL connection
	 * @param parentTables the parent tables
	 * @param parentTableKeyName the name of the key column in the parent tables
	 * @param childTableKeyName the name of the key column in the child tables
	 * @return all child tables
	 */
	static List<NestedTableTable> fetchSimilarSubtables(Connection connection, List<NestedTableTable> parentTables, String parentTableKeyName, String childDatabaseTable, String childTableKeyName) {
		try {
			
			// build the query text
			Set<Object> keys = collectKeys(parentTables, parentTableKeyName);
			String queryText = "SELECT * FROM " + childDatabaseTable + " WHERE " + childTableKeyName + " IN (" + createInClauseList(keys) + ")";
			System.out.println("executing: " + queryText);
			
			// execute the query and collect the resulting rows 
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(queryText);
			List<String> columnNames = getColumnNames(resultSet);
			List<NestedTableRow> rows = new ArrayList<NestedTableRow>();
			while (resultSet.next()) {
				NestedTableRow row = new NestedTableRow();
				row.setValues(getRowValues(resultSet));
				rows.add(row);
			}
			resultSet.close();
			statement.close();
			
			// the key column of the child table must have been fetched, otherwise we cannot associate them with parent tables
			int childKeyColumnIndex = columnNames.indexOf(childTableKeyName);
			if (childKeyColumnIndex == -1) {
				throw new RuntimeException("subtable query did not fetch the key column -- cannot associate rows");
			}
			
			// create a subtable for each row of each parent table and associate nested rows with them
			List<NestedTableTable> childTables = new ArrayList<NestedTableTable>();
			for (NestedTableTable parentTable : parentTables) {
				int parentKeyColumnIndex = forceFindColumn(parentTable, parentTableKeyName);
				for (NestedTableRow parentRow : parentTable.getRows()) {
					
					// create a child table for this row
					NestedTableTable childTable = new NestedTableTable();
					childTable.setTitle(childDatabaseTable);
					childTable.setColumnNames(columnNames);
					childTables.add(childTable);
					parentRow.getSubtables().add(childTable);
					
					// associate all matching rows
					String parentKey = parentRow.getValues().get(parentKeyColumnIndex);
					for (NestedTableRow childRow : rows) {
						String childKey = childRow.getValues().get(childKeyColumnIndex);
//						System.out.println("* " + childKey + " / " + childKey.getClass() + " / " + parentKey + " / " + parentKey.getClass());
						if (childKey.equals(parentKey)) {
//							System.out.println("+++");
							childTable.getRows().add(childRow);
						}
					}
					
				}
			}
			
			return childTables;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param tables
	 * @param keyName
	 * @return
	 */
	static Set<Object> collectKeys(List<NestedTableTable> tables, String keyName) {
		Set<Object> result = new HashSet<Object>();
		for (NestedTableTable table : tables) {
			int index = forceFindColumn(table, keyName);
			for (NestedTableRow row : table.getRows()) {
				result.add(row.getValues().get(index));
			}
		}
		return result;
	}
	
	/**
	 * @param values
	 * @return
	 */
	static String createInClauseList(Set<Object> values) {
		if (values.size() == 0) {
			throw new RuntimeException("cannot create an IN clause list from an empty set");
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Object value : values) {
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			if (value instanceof String) {
				// TODO: escaping
				builder.append("'").append(value).append("'");
			} else if (value instanceof Integer) {
				builder.append(value);
			} else {
				throw new RuntimeException("cannot handle IN clause value type: " + value.getClass().getCanonicalName());
			}
		}
		return builder.toString();
	}
	
	/**
	 * @param resultSet
	 * @return
	 */
	static List<String> getColumnNames(ResultSet resultSet) throws SQLException {
		ResultSetMetaData meta = resultSet.getMetaData();
		List<String> result = new ArrayList<String>();
		for (int i=0; i<meta.getColumnCount(); i++) {
			result.add(meta.getColumnName(1 + i));
		}
		return result;
	}

	/**
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	static List<String> getRowValues(ResultSet resultSet) throws SQLException {
		ResultSetMetaData meta = resultSet.getMetaData();
		List<String> result = new ArrayList<String>();
		for (int i=0; i<meta.getColumnCount(); i++) {
			result.add(resultSet.getString(1 + i));
		}
		return result;
	}

	/**
	 * @param table
	 * @param columnName
	 * @return
	 */
	static int forceFindColumn(NestedTableTable table, String columnName) {
		int index = table.findColumn(columnName);
		if (index == -1) {
			throw new RuntimeException("column " + columnName + " not found in table " + table.getTitle());
		}
		return index;
	}
	
}
