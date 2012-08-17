/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property.type;

/**
 * This class can be used as a general fallback for SQL types.
 */
public class UnknownSqlType implements ISqlType {

	/**
	 * the sqlType
	 */
	private final int sqlType;

	/**
	 * Constructor.
	 * @param sqlType the JDBC type code
	 */
	public UnknownSqlType(final int sqlType) {
		this.sqlType = sqlType;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IValueType#getJavaType()
	 */
	@Override
	public Class<?> getJavaType() {
		return Object.class;
	}

	/**
	 * Getter method for the sqlType.
	 * @return the sqlType
	 */
	public int getSqlType() {
		return sqlType;
	}

}
