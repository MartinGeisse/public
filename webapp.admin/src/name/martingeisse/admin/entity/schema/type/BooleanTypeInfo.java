/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import name.martingeisse.common.database.IDatabaseDescriptor;

/**
 * Type object for boolean values.
 */
public class BooleanTypeInfo extends AbstractSqlTypeInfo {

	/**
	 * The shared non-nullable instance of this class.
	 */
	public static final BooleanTypeInfo nonNullableInstance = new BooleanTypeInfo(false);

	/**
	 * The shared nullable instance of this class.
	 */
	public static final BooleanTypeInfo nullableInstance = new BooleanTypeInfo(true);
	
	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 */
	public BooleanTypeInfo(boolean nullable) {
		super(nullable);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ITypeInfo#getJavaWorkType()
	 */
	@Override
	public Class<?> getJavaWorkType() {
		return Boolean.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ITypeInfo#getJavaStorageType()
	 */
	@Override
	public Class<?> getJavaStorageType() {
		return Boolean.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.property.type.ISqlTypeInfo#getSqlTypeCode()
	 */
	@Override
	public int getSqlTypeCode() {
		return Types.BOOLEAN;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.datarow.IDataRowTypeConverter#readFromResultSet(java.sql.ResultSet, int, name.martingeisse.common.database.IDatabaseDescriptor)
	 */
	@Override
	public Object readFromResultSet(ResultSet resultSet, int index, IDatabaseDescriptor databaseDescriptor) throws SQLException {
		return resultSet.getObject(index);
	}
	
}
