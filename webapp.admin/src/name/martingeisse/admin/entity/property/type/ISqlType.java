/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property.type;

/**
 * This interface adds JDBC type information.
 * 
 * NOTE: There is no getSqlType() method to get the JDBC type
 * constant yet. The reason is that the meaning of such a method
 * is still unclear: For example, for string types, there are
 * CHAR, VARCHAR, TEXT, LONGTEXT, ... -- which should the method
 * return? That depends on how it is used, and until now it
 * isn't used at all!
 */
public interface ISqlType extends IValueType {

}
