/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

/**
 * Base type for a table fetch specifier, i.e. a clause that causes
 * a table to be fetched. The table can optionally be given an
 * alias name.
 */
public interface ITableFetchSpecifier {

	/**
	 * Getter method for the tableName.
	 * @return the tableName
	 */
	public String getTableName();

	/**
	 * Getter method for the alias.
	 * @return the alias
	 */
	public String getAlias();
	
}
