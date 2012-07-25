/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * Represents a reference to a column by name.
 */
public final class ColumnReference implements IExpression {

	/**
	 * the tableNameOrAlias
	 */
	private final String tableNameOrAlias;

	/**
	 * the columnName
	 */
	private final String columnName;

	/**
	 * Constructor.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 */
	public ColumnReference(final String tableNameOrAlias, final String columnName) {
		this.tableNameOrAlias = tableNameOrAlias;
		this.columnName = columnName;
	}

	/**
	 * Getter method for the tableNameOrAlias.
	 * @return the tableNameOrAlias
	 */
	public String getTableNameOrAlias() {
		return tableNameOrAlias;
	}

	/**
	 * Getter method for the columnName.
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.expression.IExpression#writeTo(name.martingeisse.common.sql.build.ISqlBuilder)
	 */
	@Override
	public void writeTo(final ISqlBuilder builder) {
		builder.writeColumnReference(tableNameOrAlias, columnName);
	}

}
