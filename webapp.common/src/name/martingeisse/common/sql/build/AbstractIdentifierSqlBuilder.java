/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.build;

/**
 * SQL builder for SQL dialects that have a common syntax for identifiers.
 * This class also uses dot notation as a default for a column in a table.
 */
public abstract class AbstractIdentifierSqlBuilder extends AbstractSqlBuilder {

	/**
	 * Constructor.
	 */
	public AbstractIdentifierSqlBuilder() {
	}
	
	/**
	 * Writes an identifier to the output.
	 * @param name the identifier name
	 */
	public abstract void writeIdentifier(String name);

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.build.ISqlBuilder#writeTableReference(java.lang.String)
	 */
	@Override
	public void writeTableReference(String tableName) {
		writeIdentifier(tableName);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.build.ISqlBuilder#writeTableAliasDeclaration(java.lang.String)
	 */
	@Override
	public void writeTableAliasDeclaration(String tableAlias) {
		writeIdentifier(tableAlias);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.build.ISqlBuilder#writeColumnAliasDeclaration(java.lang.String)
	 */
	@Override
	public void writeColumnAliasDeclaration(String columnAlias) {
		writeIdentifier(columnAlias);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.build.ISqlBuilder#writeColumnReference(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeColumnReference(String tableNameOrAlias, String columnName) {
		writeIdentifier(tableNameOrAlias);
		write(".");
		writeIdentifier(columnName);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.build.ISqlBuilder#writeColumnAliasReference(java.lang.String)
	 */
	@Override
	public void writeColumnAliasReference(String alias) {
		writeIdentifier(alias);
	}

}
