/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property.type;

import org.apache.wicket.util.string.StringValue;

/**
 * This interface adds special handling for types that can be used as
 * entity IDs.
 */
public interface IEntityIdType extends ISqlType {

	/**
	 * Converts an entity ID of this type from a page parameter value
	 * to a Java value.
	 * @param value the value to convert.
	 * @return the converted value
	 */
	public Object convertFromPageParameter(StringValue value);
	
}
