/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property.type;

import org.apache.wicket.util.string.StringValue;

/**
 * Type object for 32-bit integers.
 */
public final class IntegerType implements IEntityIdType {

	/**
	 * The shared instance of this class.
	 */
	public static final IntegerType instance = new IntegerType();

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IValueType#getJavaType()
	 */
	@Override
	public Class<?> getJavaType() {
		return Integer.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IEntityIdType#convertFromPageParameter(org.apache.wicket.util.string.StringValue)
	 */
	@Override
	public Object convertFromPageParameter(final StringValue value) {
		return value.toInteger();
	}

}
