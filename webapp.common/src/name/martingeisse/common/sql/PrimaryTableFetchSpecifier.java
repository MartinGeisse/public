/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

/**
 * Default implementation for {@link IPrimaryTableFetchSpecifier}.
 */
public class PrimaryTableFetchSpecifier extends AbstractTableFetchSpecifier implements IPrimaryTableFetchSpecifier {

	/**
	 * Constructor.
	 */
	public PrimaryTableFetchSpecifier() {
		super();
	}

	/**
	 * Constructor.
	 * @param tableName the name of the table to fetch
	 */
	public PrimaryTableFetchSpecifier(final String tableName) {
		super(tableName);
	}

	/**
	 * Constructor.
	 * @param tableName the name of the table to fetch
	 * @param alias the alias of the table in the query
	 */
	public PrimaryTableFetchSpecifier(final String tableName, final String alias) {
		super(tableName, alias);
	}

}
