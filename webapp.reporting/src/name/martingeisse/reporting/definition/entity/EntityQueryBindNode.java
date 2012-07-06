/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import name.martingeisse.reporting.definition.nestedtable.NestedTableTable;

/**
 * A bind node that joins another entity based on a defined link.
 */
public class EntityQueryBindNode extends AbstractEntityFetchNode {

	/**
	 * the link
	 */
	private EntityDefinitionLink link;

	/**
	 * Constructor.
	 */
	public EntityQueryBindNode() {
	}

	/**
	 * Constructor.
	 * @param link the link to use for binding
	 */
	public EntityQueryBindNode(final EntityDefinitionLink link) {
	}

	/**
	 * Getter method for the link.
	 * @return the link
	 */
	public EntityDefinitionLink getLink() {
		return link;
	}

	/**
	 * Setter method for the link.
	 * @param link the link to set
	 */
	public void setLink(final EntityDefinitionLink link) {
		this.link = link;
	}

	/**
	 * Join-fetches data from the linked table.
	 * @param connection the JDBC connection to use
	 * @param parentTables the list of parent tables
	 * @return the list of child tables
	 * @throws SQLException on SQL errors
	 */
	List<NestedTableTable> fetch(final Connection connection, final List<NestedTableTable> parentTables) throws SQLException {
		String parentKey = link.getSourceTableKeyColumnName();
		String childTable = link.getDestination().getDatabaseTableName();
		String childKey = link.getDestinationTableKeyColumnName();
		return EntityQueryInternalUtil.fetchSimilarSubtables(connection, parentTables, parentKey, childTable, childKey);
	}
	
}
