/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

/**
 * Default implementation for {@link IJoinSpecifier}.
 */
public class JoinSpecifier extends AbstractTableFetchSpecifier implements IJoinSpecifier {

	/**
	 * Constructor.
	 */
	public JoinSpecifier() {
		super();
	}

	/**
	 * Constructor.
	 * @param tableName the name of the table to fetch
	 */
	public JoinSpecifier(final String tableName) {
		super(tableName);
	}

	/**
	 * Constructor.
	 * @param tableName the name of the table to fetch
	 * @param alias the alias of the table in the query
	 */
	public JoinSpecifier(final String tableName, final String alias) {
		super(tableName, alias);
	}

}
