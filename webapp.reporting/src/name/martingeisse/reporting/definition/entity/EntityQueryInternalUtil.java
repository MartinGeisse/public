/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

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
	static List<NestedTableTable> fetchSimilarSubtables(final Connection connection, final List<NestedTableTable> parentTables, final String parentTableKeyName, final String childDatabaseTable, final String childTableKeyName) throws SQLException {

		// build the query text
		final Set<Object> keys = collectKeys(parentTables, parentTableKeyName);
		final String queryText = "SELECT * FROM " + childDatabaseTable + " WHERE " + childTableKeyName + " IN (" + createInClauseList(keys) + ")";
		System.out.println("executing: " + queryText);

		// execute the query and collect the resulting rows 
		final Statement statement = connection.createStatement();
		final ResultSet resultSet = statement.executeQuery(queryText);
		final List<String> columnNames = getColumnNames(resultSet);
		final List<NestedTableRow> rows = new ArrayList<NestedTableRow>();
		while (resultSet.next()) {
			final NestedTableRow row = new NestedTableRow();
			row.setValues(getRowValues(resultSet));
			rows.add(row);
		}
		resultSet.close();
		statement.close();

		// the key column of the child table must have been fetched, otherwise we cannot associate them with parent tables
		final int childKeyColumnIndex = columnNames.indexOf(childTableKeyName);
		if (childKeyColumnIndex == -1) {
			throw new RuntimeException("subtable query did not fetch the key column -- cannot associate rows");
		}

		// create a subtable for each row of each parent table and associate nested rows with them
		final List<NestedTableTable> childTables = new ArrayList<NestedTableTable>();
		for (final NestedTableTable parentTable : parentTables) {
			final int parentKeyColumnIndex = forceFindColumn(parentTable, parentTableKeyName);
			for (final NestedTableRow parentRow : parentTable.getRows()) {

				// create a child table for this row
				final NestedTableTable childTable = new NestedTableTable();
				childTable.setTitle(childDatabaseTable);
				childTable.setColumnNames(columnNames);
				childTables.add(childTable);
				parentRow.getSubtables().add(childTable);

				// associate all matching rows
				final String parentKey = parentRow.getValues().get(parentKeyColumnIndex);
				for (final NestedTableRow childRow : rows) {
					final String childKey = childRow.getValues().get(childKeyColumnIndex);
					//						System.out.println("* " + childKey + " / " + childKey.getClass() + " / " + parentKey + " / " + parentKey.getClass());
					if (childKey.equals(parentKey)) {
						//							System.out.println("+++");
						childTable.getRows().add(childRow);
					}
				}

			}
		}

		return childTables;
	}

	/**
	 * @param tables
	 * @param keyName
	 * @return
	 */
	static Set<Object> collectKeys(final List<NestedTableTable> tables, final String keyName) {
		final Set<Object> result = new HashSet<Object>();
		for (final NestedTableTable table : tables) {
			final int index = forceFindColumn(table, keyName);
			for (final NestedTableRow row : table.getRows()) {
				result.add(row.getValues().get(index));
			}
		}
		return result;
	}

	/**
	 * @param values
	 * @return
	 */
	static String createInClauseList(final Set<Object> values) {
		if (values.size() == 0) {
			throw new RuntimeException("cannot create an IN clause list from an empty set");
		}
		final StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (final Object value : values) {
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
	 * @param table
	 * @param columnName
	 * @return
	 */
	static int forceFindColumn(final NestedTableTable table, final String columnName) {
		final int index = table.findColumn(columnName);
		if (index == -1) {
			throw new RuntimeException("column " + columnName + " not found in table " + table.getTitle());
		}
		return index;
	}

	/**
	 * @param resultSet
	 * @return
	 */
	static List<String> getColumnNames(final ResultSet resultSet) throws SQLException {
		final ResultSetMetaData meta = resultSet.getMetaData();
		final List<String> result = new ArrayList<String>();
		for (int i = 0; i < meta.getColumnCount(); i++) {
			result.add(meta.getColumnName(1 + i));
		}
		return result;
	}

	/**
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	static List<String> getRowValues(final ResultSet resultSet) throws SQLException {
		final ResultSetMetaData meta = resultSet.getMetaData();
		final List<String> result = new ArrayList<String>();
		for (int i = 0; i < meta.getColumnCount(); i++) {
			result.add(resultSet.getString(1 + i));
		}
		return result;
	}

}
