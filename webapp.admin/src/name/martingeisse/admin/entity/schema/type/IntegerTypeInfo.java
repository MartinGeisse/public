/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

import java.math.BigInteger;
import java.sql.Types;

import org.apache.wicket.util.string.StringValue;

/**
 * Type object for integers of various sizes.
 * An integer type is defined by a number of storage
 * bytes as well as optional minimum and maximum values.
 * 
 * The number of storage bytes assumes a signed (two's complement)
 * storage format. Values larger than 8 are always mapped to
 * Java's {@link BigInteger}.
 * 
 * The min/max values are stored as 64-bit integers, based on
 * the assumption that values outside that range will not
 * typically use hard limits.
 */
public final class IntegerTypeInfo implements IEntityIdTypeInfo {

	/**
	 * the bytes
	 */
	private final int bytes;

	/**
	 * the minValue
	 */
	private final Long minValue;

	/**
	 * the maxValue
	 */
	private final Long maxValue;

	/**
	 * Constructor.
	 * @param bytes the number of storage bytes
	 */
	public IntegerTypeInfo(final int bytes) {
		this.bytes = bytes;
		this.minValue = null;
		this.maxValue = null;
	}

	/**
	 * Constructor.
	 * @param bytes the number of storage bytes
	 * @param minValue the minimum value, or null for no lower bound
	 * @param maxValue the maximum value, or null for no upper bound
	 */
	public IntegerTypeInfo(final int bytes, final Long minValue, final Long maxValue) {
		this.bytes = bytes;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	/**
	 * Getter method for the bytes.
	 * @return the bytes
	 */
	public int getBytes() {
		return bytes;
	}
	
	/**
	 * Getter method for the minValue.
	 * @return the minValue
	 */
	public Long getMinValue() {
		return minValue;
	}
	
	/**
	 * Getter method for the maxValue.
	 * @return the maxValue
	 */
	public Long getMaxValue() {
		return maxValue;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ISqlType#getSqlTypeCode()
	 */
	@Override
	public int getSqlTypeCode() {
		if (bytes <= 1) {
			return Types.TINYINT;
		} else if (bytes <= 2) {
			return Types.SMALLINT;
		} else if (bytes <= 4) {
			return Types.INTEGER;
		} else if (bytes <= 8) {
			return Types.BIGINT;
		} else {
			throw new RuntimeException("no SQL type for " + (bytes*8) + "-bit integer");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IValueType#getJavaWorkType()
	 */
	@Override
	public Class<?> getJavaWorkType() {
		if (bytes <= 4) {
			return Integer.class;
		} else if (bytes <= 8) {
			return Long.class;
		} else {
			return BigInteger.class;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IValueType#getJavaStorageType()
	 */
	@Override
	public Class<?> getJavaStorageType() {
		if (bytes <= 1) {
			return Byte.class;
		} else if (bytes <= 2) {
			return Short.class;
		} else if (bytes <= 4) {
			return Integer.class;
		} else if (bytes <= 8) {
			return Long.class;
		} else {
			return BigInteger.class;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IEntityIdTypeInfo#convertFromStringValue(org.apache.wicket.util.string.StringValue)
	 */
	@Override
	public Object convertFromStringValue(StringValue value) {
		if (bytes <= 4) {
			return value.toInteger();
		} else if (bytes <= 8) {
			return value.toLong();
		} else {
			throw new RuntimeException("cannot convert " + (bytes*8) + "-bit integer value from Wicket StringValue");
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{integer type: " + bytes + " bytes; min = " + minValue + ", max = " + maxValue + "}";
	}
	
}
