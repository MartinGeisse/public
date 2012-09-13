/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.joda.time.DateMidnight;

/**
 * Represents a date type without time-of-day information.
 */
public class DateTypeInfo extends AbstractSqlTypeInfo {

	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 */
	public DateTypeInfo(boolean nullable) {
		super(nullable);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ITypeInfo#getJavaWorkType()
	 */
	@Override
	public Class<?> getJavaWorkType() {
		return DateMidnight.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ITypeInfo#getJavaStorageType()
	 */
	@Override
	public Class<?> getJavaStorageType() {
		return DateMidnight.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ISqlTypeInfo#getSqlTypeCode()
	 */
	@Override
	public int getSqlTypeCode() {
		return Types.DATE;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.type.ISqlTypeInfo#readFromResultSet(java.sql.ResultSet, int)
	 */
	@Override
	public Object readFromResultSet(ResultSet resultSet, int index) throws SQLException {
		// TODO: time zones -- see DateTimeTypeInfo
		Date date = resultSet.getDate(index);
		return (date == null ? null : new DateMidnight(date.getTime()));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.type.AbstractSqlTypeInfo#convertForSave(java.lang.Object)
	 */
	@Override
	public Object convertForSave(Object untypedValue) {
		if (untypedValue == null) {
			return null;
		}
		DateMidnight value = (DateMidnight)untypedValue;
		return value.toDate();
	}
	
}
