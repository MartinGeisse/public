/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.states;

import name.martingeisse.reporting.definition.ITabularQuery;
import name.martingeisse.reporting.definition.UnboundTable;
import name.martingeisse.reporting.definition.nestedtable.INestedTableQuery;
import name.martingeisse.reporting.definition.nestedtable.UnboundNestedTable;
import name.martingeisse.reporting.parser.IParserStateContext;
import name.martingeisse.reporting.parser.ParserUtil;
import name.martingeisse.reporting.parser.UnexpectedElementException;

import org.xml.sax.Attributes;

/**
 * This state is active inside an unbound nested table and expects a single query element.
 */
public class NestedTableState extends AbstractParserState {

	/**
	 * the table
	 */
	private UnboundNestedTable table;

	/**
	 * Constructor.
	 */
	public NestedTableState() {
		this.table = new UnboundNestedTable();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startState(IParserStateContext context, Class<?> expectedReturnType, String namespaceUri, String name, Attributes attributes) {
		initializeReturnType(context, expectedReturnType, UnboundTable.class);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public void consumeReturnData(IParserStateContext context, Object data) {
		table.setQuery((INestedTableQuery)data);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes) {
		if (ParserUtil.isCoreElement(namespaceUri, name, "entity")) {
			context.pushState(new EntityState(), INestedTableQuery.class, namespaceUri, name, attributes);
		} else {
			throw new UnexpectedElementException(namespaceUri, name, "expected nested-table query");
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(IParserStateContext context, String namespaceUri, String name) {
		context.popState(table);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public void consumeText(IParserStateContext context, String text) {
		noTextExpected(text, "expected nested-table query");
	}

}
