/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

import org.apache.wicket.util.string.StringValue;


/**
 * This interface is implemented by types that can be used as entity IDs.
 * Currently supported: {@link IntegerTypeInfo}, {@link StringTypeInfo}.
 */
public interface IEntityIdTypeInfo extends ISqlTypeInfo {
	
	/**
	 * Converts an entity ID from a Wicket {@link StringValue} to a Java value.
	 * @param value the value to convert.
	 * @return the converted value
	 */
	public Object convertFromStringValue(StringValue value);
	
}
