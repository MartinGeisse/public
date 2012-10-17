/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.annotation.Nullable;

import name.martingeisse.common.database.IDatabaseDescriptor;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mysema.query.sql.types.AbstractType;

/**
 * Custom type implementation for Joda DateTime. See
 * {@link CustomMysqlQuerydslConfiguration} for more information.
 */
public class CustomMysqlDateTimeType extends AbstractType<DateTime> {

    /**
     * Constructor.
     */
    public CustomMysqlDateTimeType() {
        super(Types.TIMESTAMP);
    }

    /**
     * Constructor.
     * @param type the SQL type
     */
    public CustomMysqlDateTimeType(int type) {
        super(type);
    }

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#getReturnedClass()
	 */
	@Override
	public Class<DateTime> getReturnedClass() {
		return DateTime.class;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#getValue(java.sql.ResultSet, int)
	 */
	@Override
	@Nullable
	DateTime getValue(ResultSet rs, int startIndex) throws SQLException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#setValue(java.sql.PreparedStatement, int, java.lang.Object)
	 */
	@Override
	public void setValue(PreparedStatement st, int startIndex, DateTime value) throws SQLException {
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
