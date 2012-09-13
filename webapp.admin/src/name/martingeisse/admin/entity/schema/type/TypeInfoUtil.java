/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

import java.sql.Types;

/**
 * Utility methods to deal with {@link ITypeInfo}.
 */
public final class TypeInfoUtil {

	/**
	 * Prevent instantiation.
	 */
	private TypeInfoUtil() {
	}

	/**
	 * Obtains the {@link ITypeInfo} for the specified JDBC SQL type code.
	 * 
	 * Note that this method will return a 1-byte integer type for TINYINT(1),
	 * which is correct behavior. The MySQL idiom of using TINYINT(1) when you
	 * mean "boolean" is not handled by this method, so if such behavior is
	 * expected, you should instruct the MySQL database driver to do it (which it
	 * does by default). Other databases have a distinct boolean type, so mapping
	 * TINYINT(1) to boolean is incorrect for them; it actually means "1-byte
	 * integer with only a single decimal digit being displayed".
	 * 
	 * @param sqlTypeCode the type code
	 * @param size the "size" type parameter
	 * @param nullable whether the type is nullable
	 * @return the type info object
	 */
	public static ISqlTypeInfo getTypeInfoForSqlTypeCode(int sqlTypeCode, int size, boolean nullable) {
		if (size < 0) {
			throw new IllegalArgumentException("size cannot be negative");
		}
		switch (sqlTypeCode) {

		case Types.BOOLEAN:
			return (nullable ? BooleanTypeInfo.nullableInstance : BooleanTypeInfo.nonNullableInstance);
			
		case Types.BIT:
			if (size == 0) {
				return (nullable ? BooleanTypeInfo.nullableInstance : BooleanTypeInfo.nonNullableInstance);
			}
			throw new RuntimeException("cannot handle BIT(" + size + ")");
			
		case Types.TINYINT:
			return new IntegerTypeInfo(nullable, 1);
			
		case Types.SMALLINT:
			return new IntegerTypeInfo(nullable, 2);
			
		case Types.INTEGER:
			return new IntegerTypeInfo(nullable, 4);
			
		case Types.BIGINT:
			return new IntegerTypeInfo(nullable, 8);

		case Types.CHAR:
		case Types.NCHAR:
			return new StringTypeInfo(nullable, true, 0xff, true);

		case Types.VARCHAR:
		case Types.NVARCHAR:
			return new StringTypeInfo(nullable, true, 0xffff, false);
			
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CLOB:
		case Types.NCLOB:
			return new StringTypeInfo(nullable, true, null, false);
			
		case Types.DATE:
			return new DateTypeInfo(nullable);
			
		case Types.TIMESTAMP:
			return new DateTimeTypeInfo(nullable);

		default:
			return new UnknownSqlTypeInfo(nullable, sqlTypeCode);

		}

	}
	
}
