/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.states;

import name.martingeisse.reporting.definition.entity.EntityQueryOld;
import name.martingeisse.reporting.parser.IParserStateContext;
import name.martingeisse.reporting.parser.MissingAttributeException;
import name.martingeisse.reporting.parser.UnexpectedElementException;

import org.xml.sax.Attributes;

/**
 * This state is active while parsing an entity query.
 */
public class EntityState extends AbstractParserState {

	/**
	 * the query
	 */
	private final EntityQueryOld query;

	/**
	 * Constructor.
	 */
	public EntityState() {
		this.query = new EntityQueryOld();
	}

	/**
	 * Getter method for the query.
	 * @return the query
	 */
	public EntityQueryOld getQuery() {
		return query;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.states.AbstractParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startState(final IParserStateContext context, final Class<?> expectedReturnType, final String namespaceUri, final String name, final Attributes attributes) {
		initializeReturnType(context, expectedReturnType, EntityQueryOld.class);
		
		query.setEntityName(attributes.getValue("", "name"));
		if (query.getEntityName() == null) {
			throw new MissingAttributeException(namespaceUri, name, "name");
		}
		
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
		throw new UnexpectedElementException(namespaceUri, namespaceUri, "... TODO ...");
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
		noTextExpected(text, "... TODO ...");
	}

}
