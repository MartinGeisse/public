/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.states;

import name.martingeisse.reporting.definition.entity.definition.EntityDefinition;
import name.martingeisse.reporting.definition.entity.definition.EntityDefinitionLink;
import name.martingeisse.reporting.definition.entity.definition.EntityDefinitionTable;
import name.martingeisse.reporting.definition.entity.query.EntityQuery;
import name.martingeisse.reporting.definition.entity.query.EntityQueryFetchClause;
import name.martingeisse.reporting.parser.IParserStateContext;
import name.martingeisse.reporting.parser.MissingAttributeException;
import name.martingeisse.reporting.parser.ParserUtil;
import name.martingeisse.reporting.parser.UnexpectedElementException;

import org.xml.sax.Attributes;

/**
 * This state is active while parsing an entity query.
 */
public class EntityState extends AbstractParserState {

	/**
	 * the query
	 */
	private final EntityQuery query;

	/**
	 * Constructor.
	 */
	public EntityState() {
		this.query = new EntityQuery();
	}

	/**
	 * Getter method for the query.
	 * @return the query
	 */
	public EntityQuery getQuery() {
		return query;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.states.AbstractParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startState(final IParserStateContext context, final Class<?> expectedReturnType, final String namespaceUri, final String name, final Attributes attributes) {
		initializeReturnType(context, expectedReturnType, EntityQuery.class);
		
		// --- TODO ---------------------------------
		
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
		
		query.setEntityDefinition(entityDefinition);
		
		// --- TODO ---------------------------------
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.states.AbstractParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public void consumeReturnData(final IParserStateContext context, final Object data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.states.AbstractParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(final IParserStateContext context, final String namespaceUri, final String name, final Attributes attributes) {
		if (ParserUtil.isCoreElement(namespaceUri, name, "fetch")) {
			String path = attributes.getValue("", "path");
			if (path == null) {
				throw new MissingAttributeException(namespaceUri, name, "path");
			}
			EntityQueryFetchClause fetchClause = new EntityQueryFetchClause(path);
			query.getFetchClauses().add(fetchClause);
			context.pushState(new EmptyState(), null, namespaceUri, name, attributes);
		} else {
			throw new UnexpectedElementException(namespaceUri, namespaceUri, "expected <fetch> clauses");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.states.AbstractParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(final IParserStateContext context, final String namespaceUri, final String name) {
		popStateWithAdaptedReturnData(context, query);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.states.AbstractParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public void consumeText(final IParserStateContext context, final String text) {
		noTextExpected(text, "expected <fetch> clauses");
	}

}
