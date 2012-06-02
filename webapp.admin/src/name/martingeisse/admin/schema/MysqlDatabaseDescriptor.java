/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.schema;


/**
 * Concrete implementation of {@link AbstractDatabaseDescriptor} for
 * MySQL databases.
 */
public class MysqlDatabaseDescriptor extends AbstractDatabaseDescriptor {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.schema.AbstractDatabaseDescriptor#getIdentifierBeginQuoteCharacter()
	 */
	@Override
	public char getIdentifierBeginQuoteCharacter() {
		return '`';
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.schema.AbstractDatabaseDescriptor#getIdentifierEndQuoteCharacter()
	 */
	@Override
	public char getIdentifierEndQuoteCharacter() {
		return '`';
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.schema.AbstractDatabaseDescriptor#getDefaultOrderClause()
	 */
	@Override
	public String getDefaultOrderClause() {
		// in case surrounding whitespace is missing...
		return " ";
	}

}
