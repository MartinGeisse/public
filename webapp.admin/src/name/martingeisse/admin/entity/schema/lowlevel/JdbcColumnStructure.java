/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.lowlevel;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import name.martingeisse.admin.entity.schema.type.ISqlTypeInfo;
import name.martingeisse.admin.entity.schema.type.TypeInfoUtil;

/**
 * The low-level structure of a JDBC column.
 */
public final class JdbcColumnStructure {

	/**
	 * the selector
	 */
	private final JdbcColumnSelector selector;

	/**
	 * the sqlType
	 */
	private final int sqlType;

	/**
	 * the typeName
	 */
	private final String typeName;

	/**
	 * the size
	 */
	private final int size;

	/**
	 * the fractionalDigits
	 */
	private final int fractionalDigits;

	/**
	 * the radix
	 */
	private final int radix;

	/**
	 * the nullable
	 */
	private final boolean nullable;

	/**
	 * Constructor that takes column information from the current row of the
	 * specified result set. The result set should use the format specified for
	 * {@link DatabaseMetaData#getColumns(String, String, String, String)}.
	 * 
	 * @param resultSet the result set
	 * @throws SQLException on SQL errors
	 */
	public JdbcColumnStructure(final ResultSet resultSet) throws SQLException {
		this.selector = new JdbcColumnSelector(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
		this.sqlType = resultSet.getInt(5);
		this.typeName = resultSet.getString(6);
		this.size = resultSet.getInt(7);
		this.fractionalDigits = resultSet.getInt(9);
		this.radix = resultSet.getInt(10);
		if (resultSet.getInt(11) == DatabaseMetaData.columnNoNulls) {
			this.nullable = false;
		} else if (resultSet.getInt(11) == DatabaseMetaData.columnNullable) {
			this.nullable = true;
		} else {
			throw new RuntimeException("cannot determine nullability for column: " + selector);
		}
	}

	/**
	 * Getter method for the selector.
	 * @return the selector
	 */
	public JdbcColumnSelector getSelector() {
		return selector;
	}

	/**
	 * Getter method for the sqlType.
	 * @return the sqlType
	 */
	public int getSqlType() {
		return sqlType;
	}

	/**
	 * Getter method for the typeName.
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Getter method for the size.
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Getter method for the fractionalDigits.
	 * @return the fractionalDigits
	 */
	public int getFractionalDigits() {
		return fractionalDigits;
	}

	/**
	 * Getter method for the radix.
	 * @return the radix
	 */
	public int getRadix() {
		return radix;
	}

	/**
	 * Getter method for the nullable.
	 * @return the nullable
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * Determines the high-level {@link ISqlTypeInfo} for this column.
	 * @param hasDefaultTimeZone whether the database has a known default time zone
	 * @return the high-level type for this current column
	 */
	public ISqlTypeInfo determineHighlevelType(boolean hasDefaultTimeZone) {
		return TypeInfoUtil.getTypeInfoForSqlTypeCode(sqlType, size, nullable, hasDefaultTimeZone);
	}

}
