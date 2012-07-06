/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.definition.nestedtable.INestedTableQuery;
import name.martingeisse.reporting.definition.nestedtable.INestedTableResult;
import name.martingeisse.reporting.definition.nestedtable.NestedTableResult;
import name.martingeisse.reporting.definition.nestedtable.NestedTableTable;

/**
 * This query represents a high-level way to express an SQL query.
 */
public final class EntityQuery implements INestedTableQuery {

	/**
	 * the rootNode
	 */
	private EntityQueryRootNode rootNode;
	
	/**
	 * Constructor.
	 */
	public EntityQuery() {
	}

	/**
	 * Getter method for the rootNode.
	 * @return the rootNode
	 */
	public EntityQueryRootNode getRootNode() {
		return rootNode;
	}
	
	/**
	 * Setter method for the rootNode.
	 * @param rootNode the rootNode to set
	 */
	public void setRootNode(EntityQueryRootNode rootNode) {
		this.rootNode = rootNode;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.nestedtable.INestedTableQuery#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public INestedTableResult bindToData(final DataSources dataSources) {

		// fetch the root table
		final Connection connection = dataSources.getConnection("default");
		final NestedTableTable rootTable = EntityQueryInternalUtil.fetchRootTable(connection, entityDefinition.getTable().getDatabaseTableName());
		final List<NestedTableTable> rootTables = Arrays.asList(rootTable);

		// fetch subtables
		for (EntityQueryFetchClause fetchClause : fetchClauses) {
			fetchSubtables(connection, rootTables, entityDefinition.getTable(), fetchClause.getPath(), 0);
		}

		// build the result
		final NestedTableResult result = new NestedTableResult(rootTable);
		return result;

	}
	
	/**
	 * @param connection
	 * @param rootTables
	 * @param tableDefinition
	 */
	private void fetchSubtables(Connection connection, List<NestedTableTable> tables, EntityDefinitionTable tableDefinition, EntityPath path, int pathIndex) {

		// stop if the path is finished
		if (pathIndex == path.getSegmentCount()) {
			return;
		}

		// determine where to move next
		String pathSegment = path.getSegment(pathIndex);
		EntityDefinitionLink link = tableDefinition.getLinks().get(pathSegment);
		if (link == null) {
			throw new RuntimeException("unknown entity link: " + pathSegment);
		}
		EntityDefinitionTable subtableDefinition = link.getDestinationTable();
		
		// fetch one level of tables
		List<NestedTableTable> subtables = EntityQueryInternalUtil.fetchSimilarSubtables(connection, tables,
			link.getSourceTableKeyColumnName(), subtableDefinition.getDatabaseTableName(), link.getDestinationTableKeyColumnName());
		
		// recursively fetch subtables
		fetchSubtables(connection, subtables, subtableDefinition, path, pathIndex + 1);
		
	}
	
}
