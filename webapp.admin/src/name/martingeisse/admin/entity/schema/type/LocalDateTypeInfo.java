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

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Represents a date type without time-of-day information.
 */
public class LocalDateTypeInfo extends AbstractSqlTypeInfo {

	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 */
	public LocalDateTypeInfo(boolean nullable) {
		super(nullable);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ITypeInfo#getJavaWorkType()
	 */
	@Override
	public Class<?> getJavaWorkType() {
		return LocalDate.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ITypeInfo#getJavaStorageType()
	 */
	@Override
	public Class<?> getJavaStorageType() {
		return LocalDate.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ISqlTypeInfo#getSqlTypeCode()
	 */
	@Override
	public int getSqlTypeCode() {
		return Types.DATE;
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
		DateTimeFormatter formatter = DateTimeFormat.forPattern(databaseDescriptor.getDatePattern());
		LocalDate result = formatter.parseLocalDate(stringValue);
		return result;
			
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.type.AbstractSqlTypeInfo#convertForSave(java.lang.Object, name.martingeisse.common.database.IDatabaseDescriptor)
	 */
	@Override
	public Object convertForSave(Object value, IDatabaseDescriptor databaseDescriptor) {
		if (value == null) {
			return null;
		} else if (value instanceof LocalDate) {
			LocalDate localDate = (LocalDate)value;
			DateTimeFormatter formatter = DateTimeFormat.forPattern(databaseDescriptor.getDatePattern());
			String formatted = formatter.print(localDate);
			return formatted;
		} else {
			throw new IllegalArgumentException("invalid value for date conversion: " + value + " (class: " + value.getClass() + ")");
		}
	}
	
}
