/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import name.martingeisse.common.database.IDatabaseDescriptor;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Represents a date type including time-of-day information
 * but with unknown time zone.
 */
public class LocalDateTimeTypeInfo extends AbstractSqlTypeInfo {

	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 */
	public LocalDateTimeTypeInfo(boolean nullable) {
		super(nullable);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ITypeInfo#getJavaWorkType()
	 */
	@Override
	public Class<?> getJavaWorkType() {
		return LocalDateTime.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ITypeInfo#getJavaStorageType()
	 */
	@Override
	public Class<?> getJavaStorageType() {
		return LocalDateTime.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ISqlTypeInfo#getSqlTypeCode()
	 */
	@Override
	public int getSqlTypeCode() {
		return Types.TIMESTAMP;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRowTypeConverter#readFromResultSet(java.sql.ResultSet, int, name.martingeisse.common.database.IDatabaseDescriptor)
	 */
	@Override
	public Object readFromResultSet(ResultSet resultSet, int index, IDatabaseDescriptor databaseDescriptor) throws SQLException {
		
		// get the value as a string and handle null
		String stringValue = resultSet.getString(index);
		if (stringValue == null) {
			return null;
		}
		
		// parse the value
		DateTimeFormatter formatter = DateTimeFormat.forPattern(databaseDescriptor.getDateTimePattern());
		LocalDateTime result = formatter.parseLocalDateTime(stringValue);
		return result;

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.type.ISqlTypeInfo#convertForSave(java.lang.Object, name.martingeisse.common.database.IDatabaseDescriptor)
	 */
	@Override
	public Object convertForSave(Object value, IDatabaseDescriptor databaseDescriptor) {
		if (value == null) {
			return null;
		} else if (value instanceof LocalDateTime) {
			LocalDateTime localDateTime = (LocalDateTime)value;
			DateTimeFormatter formatter = DateTimeFormat.forPattern(databaseDescriptor.getDateTimePattern());
			String formatted = formatter.print(localDateTime);
			return formatted;
		} else {
			throw new IllegalArgumentException("invalid value for date-time conversion: " + value + " (class: " + value.getClass() + ")");
		}
	}
	
}
