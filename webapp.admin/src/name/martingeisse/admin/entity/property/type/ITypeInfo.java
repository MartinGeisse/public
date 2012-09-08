/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property.type;

/**
 * Generic super-interface for type information. All types have
 * the following information associated with them:
 * 
 * - a Java "work" type, i.e. a Java type that is used when working
 *   with values of this type.
 *   
 * - a Java "storage" type, i.e. a Java type that is used when storing
 *   values of this type.
 *   
 * The two will often be the same; however, some types use a more compact
 * storage type. For example, and 8-bit signed integer would use Java's
 * "byte" type for storage but "int" as the working type.
 */
public interface ITypeInfo {

	/**
	 * @return the class object for this type when working with Java values
	 */
	public Class<?> getJavaWorkType();

	/**
	 * @return the class object for this type when storing Java values
	 */
	public Class<?> getJavaStorageType();

}
