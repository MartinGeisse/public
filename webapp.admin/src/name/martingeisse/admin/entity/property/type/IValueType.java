/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property.type;

/**
 * Generic super-interface for type information.
 */
public interface IValueType {

	/**
	 * @return the class object for this type when dealing with Java values
	 */
	public Class<?> getJavaType();
	
}
