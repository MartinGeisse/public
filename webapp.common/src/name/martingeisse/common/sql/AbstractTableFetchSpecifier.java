/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

/**
 * Base implementation for {@link ITableFetchSpecifier}.
 */
public class AbstractTableFetchSpecifier implements ITableFetchSpecifier {

	/**
	 * the tableName
	 */
	private String tableName;

	/**
	 * the alias
	 */
	private String alias;

	/**
	 * Constructor.
	 */
	public AbstractTableFetchSpecifier() {
	}

	/**
	 * Constructor.
	 * @param tableName the name of the table to fetch
	 */
	public AbstractTableFetchSpecifier(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Constructor.
	 * @param tableName the name of the table to fetch
	 * @param alias the alias of the table in the query
	 */
	public AbstractTableFetchSpecifier(final String tableName, final String alias) {
		this.tableName = tableName;
		this.alias = alias;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.ITableFetchSpecifier#getTableName()
	 */
	@Override
	public String getTableName() {
		return tableName;
	}

	/**
	 * Setter method for the tableName.
	 * @param tableName the tableName to set
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.ITableFetchSpecifier#getAlias()
	 */
	@Override
	public String getAlias() {
		return alias;
	}

	/**
	 * Setter method for the alias.
	 * @param alias the alias to set
	 */
	public void setAlias(final String alias) {
		this.alias = alias;
	}

}
