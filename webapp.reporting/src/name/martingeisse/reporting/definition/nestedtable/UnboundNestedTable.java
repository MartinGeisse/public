/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.definition.entity.EntityDefinition;
import name.martingeisse.reporting.definition.entity.EntityDefinitionLink;
import name.martingeisse.reporting.definition.entity.EntityDefinitionTable;
import name.martingeisse.reporting.definition.entity.EntityQuery;
import name.martingeisse.reporting.definition.entity.EntityQueryFetchClause;
import name.martingeisse.reporting.document.AbstractFigureItem;
import name.martingeisse.reporting.document.NestedTable;

/**
 * A renderable nested table.
 */
public class UnboundNestedTable extends AbstractFigureItem {

	/**
	 * the data
	 */
	private INestedTableQuery query;

	/**
	 * Constructor.
	 */
	public UnboundNestedTable() {
	}

	/**
	 * Constructor.
	 * @param query the nested-table query
	 */
	public UnboundNestedTable(final INestedTableQuery query) {
		this.query = query;
	}

	/**
	 * Getter method for the query.
	 * @return the query
	 */
	public INestedTableQuery getQuery() {
		return query;
	}

	/**
	 * Setter method for the query.
	 * @param query the query to set
	 */
	public void setQuery(final INestedTableQuery query) {
		this.query = query;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public NestedTable bindToData(final DataSources dataSources) {
		
		EntityDefinitionTable rootTable = new EntityDefinitionTable();
		rootTable.setDatabaseTableName("phpbb_forums");

		EntityDefinitionTable subForumTable = new EntityDefinitionTable();
		subForumTable.setDatabaseTableName("phpbb_forums");
		rootTable.getLinks().put("Child", new EntityDefinitionLink(subForumTable, "forum_id", "parent_id"));

		EntityDefinitionTable trackTable = new EntityDefinitionTable();
		trackTable.setDatabaseTableName("phpbb_forums_track");
		rootTable.getLinks().put("Track", new EntityDefinitionLink(trackTable, "forum_id", "forum_id"));
		
		EntityDefinition entityDefinition = new EntityDefinition();
		entityDefinition.setName("Forum");
		entityDefinition.setTable(rootTable);
		
		EntityQuery q = new EntityQuery(); // TODO
		q.setEntityDefinition(entityDefinition);
		q.getFetchClauses().add(new EntityQueryFetchClause("Child"));
		return new NestedTable(q.bindToData(dataSources).makeExplicitOnDemand());
	}

}
