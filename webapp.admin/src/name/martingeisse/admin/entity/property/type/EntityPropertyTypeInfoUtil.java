/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property.type;

import org.apache.wicket.util.string.StringValue;

/**
 * Utility methods to deal with the types of entity properties.
 */
public final class EntityPropertyTypeInfoUtil {

	/**
	 * Prevent instantiation.
	 */
	private EntityPropertyTypeInfoUtil() {
	}
	
	/**
	 * Converts an entity ID of the specified type from a page parameter value to a Java value.
	 * @param type the type of the value
	 * @param value the value to convert.
	 * @return the converted value
	 */
	public static Object convertFromStringValue(IEntityIdTypeInfo type, StringValue value) {
		Class<?> workType = type.getJavaWorkType();
		if (workType == Integer.class) {
			return value.toInteger();
		} else if (workType == Long.class) {
			return value.toLong();
		} else if (workType == String.class) {
			return value.toString();
		} else {
			throw new RuntimeException("cannot convert value " + value + " of type " + type + " from Wicket StringValue");
		}
	}
	
}
