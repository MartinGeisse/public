/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class can be used as a general fallback for SQL types.
 */
public class UnknownSqlTypeInfo extends AbstractSqlTypeInfo {

	/**
	 * the sqlTypeCode
	 */
	private final int sqlTypeCode;

	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 * @param sqlTypeCode the JDBC type code
	 */
	public UnknownSqlTypeInfo(final boolean nullable, final int sqlTypeCode) {
		super(nullable);
		this.sqlTypeCode = sqlTypeCode;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IValueType#getJavaWorkType()
	 */
	@Override
	public Class<?> getJavaWorkType() {
		return Object.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IValueType#getJavaStorageType()
	 */
	@Override
	public Class<?> getJavaStorageType() {
		return Object.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ISqlType#getSqlTypeCode()
	 */
	@Override
	public int getSqlTypeCode() {
		return sqlTypeCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{unknown SQL type; code = " + sqlTypeCode + "}";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.type.ISqlTypeInfo#readFromResultSet(java.sql.ResultSet, int)
	 */
	@Override
	public Object readFromResultSet(ResultSet resultSet, int index) throws SQLException {
		return resultSet.getObject(index);
	}
	
}
