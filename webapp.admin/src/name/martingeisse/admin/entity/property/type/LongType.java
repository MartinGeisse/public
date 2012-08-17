/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property.type;

import org.apache.wicket.util.string.StringValue;

/**
 * Type object for 64-bit integers.
 */
public final class LongType implements IEntityIdType {

	/**
	 * The shared instance of this class.
	 */
	public static final LongType instance = new LongType();

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IValueType#getJavaType()
	 */
	@Override
	public Class<?> getJavaType() {
		return Long.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IEntityIdType#convertFromPageParameter(org.apache.wicket.util.string.StringValue)
	 */
	@Override
	public Object convertFromPageParameter(final StringValue value) {
		return value.toLongObject();
	}

}
