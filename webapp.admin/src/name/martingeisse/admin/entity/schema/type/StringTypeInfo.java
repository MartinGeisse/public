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

import org.apache.wicket.util.string.StringValue;

/**
 * Type object for strings. A string type is defined by three
 * properties:
 * 
 * - a flag that indicates whether the empty string is an allowed value
 * - an optional maximum number of characters
 * - a flag that indicates whether the string is always space-padded to
 *   its maximum length (only valid if a maximum length is specified).
 */
public final class StringTypeInfo extends AbstractEntityIdTypeInfo {

	/**
	 * the allowEmpty
	 */
	private final boolean allowEmpty;

	/**
	 * the maxLength
	 */
	private final Integer maxLength;
	
	/**
	 * the padded
	 */
	private final boolean padded;

	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 * @param allowEmpty whether this type allows the empty string
	 */
	public StringTypeInfo(final boolean nullable, final boolean allowEmpty) {
		this(nullable, allowEmpty, null, false);
	}

	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 * @param allowEmpty whether this type allows the empty string
	 * @param maxLength the maximum string length, or null for unbounded length
	 * @param padded whether the string is always space-padded to its maximum length.
	 * Must be false if maxLength is null.
	 */
	public StringTypeInfo(final boolean nullable, final boolean allowEmpty, final Integer maxLength, final boolean padded) {
		super(nullable);
		if (maxLength != null && maxLength < 0) {
			throw new IllegalArgumentException("trying to construct string type with negative max length");
		}
		if (padded && maxLength == null) {
			throw new IllegalArgumentException("trying to construct padded string type without max length");
		}
		this.allowEmpty = allowEmpty;
		this.maxLength = maxLength;
		this.padded = padded;
	}

	/**
	 * Getter method for the allowEmpty.
	 * @return the allowEmpty
	 */
	public boolean isAllowEmpty() {
		return allowEmpty;
	}

	/**
	 * Getter method for the maxLength.
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * Getter method for the padded.
	 * @return the padded
	 */
	public boolean isPadded() {
		return padded;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ISqlType#getSqlTypeCode()
	 */
	@Override
	public int getSqlTypeCode() {
		if (padded) {
			if (maxLength < 0x100) {
				return Types.CHAR;
			} else {
				throw new RuntimeException("no SQL type code for padded string type with maxLength " + maxLength);
			}
		} else if (maxLength < 0x10000) {
			return Types.VARCHAR;
		} else {
			return Types.CLOB;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IValueType#getJavaWorkType()
	 */
	@Override
	public Class<?> getJavaWorkType() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IValueType#getJavaStorageType()
	 */
	@Override
	public Class<?> getJavaStorageType() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IEntityIdTypeInfo#convertFromStringValue(org.apache.wicket.util.string.StringValue)
	 */
	@Override
	public Object convertFromStringValue(StringValue value) {
		return value.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{string type; allowEmpty = " + allowEmpty + ", maxLength = " + maxLength + ", padded = " + padded + "}";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRowTypeConverter#readFromResultSet(java.sql.ResultSet, int, name.martingeisse.common.database.IDatabaseDescriptor)
	 */
	@Override
	public Object readFromResultSet(ResultSet resultSet, int index, IDatabaseDescriptor databaseDescriptor) throws SQLException {
		return resultSet.getObject(index);
	}
	
}