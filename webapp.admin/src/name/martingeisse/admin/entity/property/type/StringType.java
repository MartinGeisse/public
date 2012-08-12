/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property.type;

import org.apache.wicket.util.string.StringValue;

/**
 * Type object for strings.
 */
public final class StringType implements IEntityIdType {

	/**
	 * The shared instance of this class.
	 */
	public static final StringType instance = new StringType();

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IValueType#getJavaType()
	 */
	@Override
	public Class<?> getJavaType() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.IEntityIdType#convertFromPageParameter(org.apache.wicket.util.string.StringValue)
	 */
	@Override
	public Object convertFromPageParameter(final StringValue value) {
		return value.toString();
	}

}
