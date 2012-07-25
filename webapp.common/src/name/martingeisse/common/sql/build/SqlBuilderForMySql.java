/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.build;

/**
 * The {@link ISqlBuilder} implementation for MySQL.
 */
public class SqlBuilderForMySql extends AbstractIdentifierSqlBuilder {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.build.AbstractIdentifierSqlBuilder#writeIdentifier(java.lang.String)
	 */
	@Override
	public void writeIdentifier(String name) {
		write("`");
		write(name);
		write("`");
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.build.ISqlBuilder#writeStringLiteral(java.lang.String)
	 */
	@Override
	public void writeStringLiteral(String value) {
		write("'");
		write(value.replace("\\", "\\\\").replace("'", "\\'")); // TODO: safe?
		write("'");
	}

}
