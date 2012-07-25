/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.build;

/**
 * This interface is implemented by database-specific SQL formatters.
 */
public interface ISqlBuilder {
	
	/**
	 * Writes a piece of SQL directly to the output.
	 * @param s the SQL fragment to write
	 */
	public void write(String s);
	
	/**
	 * Writes a reference to a table
	 * @param tableName the name of the table
	 */
	public void writeTableReference(String tableName);
	
	/**
	 * Writes a declaration of a table alias.
	 * @param tableAlias the declared alias of the table
	 */
	public void writeTableAliasDeclaration(String tableAlias);
	
	/**
	 * Writes a declaration of a column alias.
	 * @param columnAlias the declared alias of the column
	 */
	public void writeColumnAliasDeclaration(String columnAlias);
	
	/**
	 * Writes a reference to a column of a table.
	 * @param tableNameOrAlias the table name or table alias
	 * @param columnName the name of the column
	 */
	public void writeColumnReference(String tableNameOrAlias, String columnName);
	
	/**
	 * Writes a reference to a column alias
	 * @param alias the alias of the column
	 */
	public void writeColumnAliasReference(String alias);

	/**
	 * Writes a string literal to the output.
	 * @param value the value of the string literal.
	 */
	public void writeStringLiteral(String value);
	
}
