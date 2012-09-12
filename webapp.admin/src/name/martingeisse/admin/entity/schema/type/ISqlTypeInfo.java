/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

/**
 * This interface adds JDBC type information.
 */
public interface ISqlTypeInfo extends ITypeInfo {

	/**
	 * Getter method for the JDBC SQL type code.
	 * @return the SQL type code
	 */
	public int getSqlTypeCode();
	
}
