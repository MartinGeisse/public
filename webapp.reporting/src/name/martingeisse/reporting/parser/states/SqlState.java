/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.states;

import org.xml.sax.Attributes;

import name.martingeisse.reporting.definition.ITabularQuery;
import name.martingeisse.reporting.definition.SqlQuery;
import name.martingeisse.reporting.parser.IParserStateContext;

/**
 * This class expects text content and wraps it in an {@link SqlQuery}
 * (a kind of {@link ITabularQuery}, used to fill tables).
 * 
 * TODO: support different data sources than "default".
 */
public class SqlState extends AbstractTextOnlyState {

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractTextOnlyState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startState(IParserStateContext context, Class<?> expectedReturnType, String namespaceUri, String name, Attributes attributes) {
		initializeReturnType(context, expectedReturnType, SqlQuery.class);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractTextOnlyState#createReturnData(java.lang.String)
	 */
	@Override
	protected Object createReturnData(String text) {
		return new SqlQuery("default", text);
	}

}
