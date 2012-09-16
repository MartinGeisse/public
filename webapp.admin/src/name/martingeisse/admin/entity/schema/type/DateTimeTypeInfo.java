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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Represents a date type including time-of-day information
 * with a known time zone.
 * 
 * Note: JDBC drivers suck royally when it comes to date-time
 * handling. There is are so many implicit rules, redundant
 * configuration options, incompatibility between vendors and
 * violation of the standard that we're better off taking the
 * value as a string (so we get exactly the information stored
 * in the table cell) and parsing it ourselves.
 */
public class DateTimeTypeInfo extends AbstractSqlTypeInfo {

	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 */
	public DateTimeTypeInfo(boolean nullable) {
		super(nullable);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ITypeInfo#getJavaWorkType()
	 */
	@Override
	public Class<?> getJavaWorkType() {
		return DateTime.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ITypeInfo#getJavaStorageType()
	 */
	@Override
	public Class<?> getJavaStorageType() {
		return DateTime.class;
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
		String stringValue = resultSet.getString(index);
		if (stringValue == null) {
			return null;
		} else {
			DateTimeFormatter formatter = getFormatter(databaseDescriptor);
			DateTime result = formatter.parseDateTime(stringValue);
			return result;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.type.ISqlTypeInfo#convertForSave(java.lang.Object, name.martingeisse.common.database.IDatabaseDescriptor)
	 */
	@Override
	public Object convertForSave(Object value, IDatabaseDescriptor databaseDescriptor) {
		if (value == null) {
			return null;
		} else if (value instanceof ReadableInstant) {
			ReadableInstant readableInstant = (ReadableInstant)value;
			DateTimeFormatter formatter = getFormatter(databaseDescriptor);
			String formatted = formatter.print(readableInstant);
			return formatted;
		} else {
			throw new IllegalArgumentException("invalid value for date-time conversion: " + value + " (class: " + value.getClass() + ")");
		}
	}

	/**
	 * Obtains a date-time formatter for instants for the specified database.
	 * The formatter uses the default time zone of the database and the vendor-specific datetime format.
	 */
	private static DateTimeFormatter getFormatter(IDatabaseDescriptor databaseDescriptor) {
		DateTimeZone timeZone = databaseDescriptor.getDefaultTimeZone();
		if (timeZone == null) {
			throw new IllegalStateException("no database time zone");
		}
		return DateTimeFormat.forPattern(databaseDescriptor.getDateTimePattern()).withZone(timeZone);
	}
}