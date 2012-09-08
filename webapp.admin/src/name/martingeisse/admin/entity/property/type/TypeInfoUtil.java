/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property.type;

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
	 * @param sqlTypeCode the type code
	 * @return the type info object
	 */
	public static ISqlTypeInfo getTypeInfoForSqlTypeCode(int sqlTypeCode) {
		switch (sqlTypeCode) {

		case Types.TINYINT:
			return new IntegerTypeInfo(1);
			
		case Types.SMALLINT:
			return new IntegerTypeInfo(2);
			
		case Types.INTEGER:
			return new IntegerTypeInfo(4);
			
		case Types.BIGINT:
			return new IntegerTypeInfo(8);

		case Types.CHAR:
		case Types.NCHAR:
			return new StringTypeInfo(true, 0xff, true);

		case Types.VARCHAR:
		case Types.NVARCHAR:
			return new StringTypeInfo(true, 0xffff, false);
			
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CLOB:
		case Types.NCLOB:
			return new StringTypeInfo(true, null, false);

		default:
			return new UnknownSqlTypeInfo(sqlTypeCode);

		}

	}
	
}
