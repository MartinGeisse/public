/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.database;

/**
 * Concrete implementation of {@link AbstractDatabaseDescriptor} for
 * PostgreSQL databases.
 */
public class PostgresDatabaseDescriptor extends AbstractDatabaseDescriptor {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.schema.AbstractDatabaseDescriptor#getIdentifierBeginQuoteCharacter()
	 */
	@Override
	public char getIdentifierBeginQuoteCharacter() {
		return '"';
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.schema.AbstractDatabaseDescriptor#getIdentifierEndQuoteCharacter()
	 */
	@Override
	public char getIdentifierEndQuoteCharacter() {
		return '"';
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.schema.AbstractDatabaseDescriptor#getDefaultOrderClause()
	 */
	@Override
	public String getDefaultOrderClause() {
		// TODO problem: there is no column that is guaranteed to appear in all
		// tables, so we need to take an arbitrary column from the specific table being fetched
		throw new RuntimeException("TODO");
	}
	
}
