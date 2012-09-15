/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

import name.martingeisse.common.database.IDatabaseDescriptor;
import name.martingeisse.common.datarow.IDataRowTypeConverter;

/**
 * This interface adds JDBC type information.
 * 
 * The data row type converter implementation must convert its result set
 * field to an instance of the type returned by {{@link #getJavaWorkType()}.
 */
public interface ISqlTypeInfo extends ITypeInfo, IDataRowTypeConverter {

	/**
	 * Getter method for the JDBC SQL type code.
	 * @return the SQL type code
	 */
	public int getSqlTypeCode();
	
	/**
	 * Converts an instance of this type for saving into the database.
	 * This method basically does the inverse conversion as
	 * {{@link #readFromResultSet(java.sql.ResultSet, int, IDatabaseDescriptor)}.
	 * 
	 * @param value the value to convert. Should be an instance of the
	 * type returned by {{@link #getJavaWorkType()}.
	 * @param databaseDescriptor the database descriptor for the database to which
	 * the row will be saved
	 * @return the JDBC-compatible value to save into the database
	 */
	public Object convertForSave(Object value, IDatabaseDescriptor databaseDescriptor);
	
}
