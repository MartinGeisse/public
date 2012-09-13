/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.joda.time.DateTime;

/**
 * Represents a date type including time-of-day information.
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
	 * @see name.martingeisse.admin.entity.schema.type.ISqlTypeInfo#readFromResultSet(java.sql.ResultSet, int)
	 */
	@Override
	public Object readFromResultSet(ResultSet resultSet, int index) throws SQLException {
		// TODO: this conversion uses an implicit time zone to create the "Date" object
		// AND an implicit time zone to create the DateMidnight object.
		// -> Where does the former come from?
		// -> might we want a different one to be used for the former?
		// -> might we want a different one to be used for the latter?
		Timestamp date = resultSet.getTimestamp(index);
		// TODO: same for back-conversion -- this seems to happen automatically at the moment.
		
		return (date == null ? null : new DateTime(date.getTime()));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.type.AbstractSqlTypeInfo#convertForSave(java.lang.Object)
	 */
	@Override
	public Object convertForSave(Object untypedValue) {
		if (untypedValue == null) {
			return null;
		}
		DateTime value = (DateTime)untypedValue;
		return value.toDate();
	}
	
}
