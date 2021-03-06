/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.states;

import name.martingeisse.reporting.definition.keycount.IKeyCountQuery;
import name.martingeisse.reporting.definition.keycount.UnboundChartBlock;
import name.martingeisse.reporting.parser.IParserStateContext;
import name.martingeisse.reporting.parser.ParserUtil;
import name.martingeisse.reporting.parser.UnexpectedElementException;
import name.martingeisse.reporting.parser.UnexpectedReturnDataException;

import org.xml.sax.Attributes;

/**
 * This state is active inside an unbound pie chart and expects a
 * single query element. The expected type is a key/count query
 * ({@link IKeyCountQuery}).
 */
public class PieChartState extends AbstractParserState {

	/**
	 * the chartBlock
	 */
	private UnboundChartBlock chartBlock;

	/**
	 * Constructor.
	 */
	public PieChartState() {
		this.chartBlock = new UnboundChartBlock();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startState(IParserStateContext context, Class<?> expectedReturnType, String namespaceUri, String name, Attributes attributes) {
		initializeReturnType(context, expectedReturnType, UnboundChartBlock.class);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public void consumeReturnData(IParserStateContext context, Object data) {
		if (data instanceof IKeyCountQuery) {
			chartBlock.setQuery((IKeyCountQuery)data);
		} else {
			throw new UnexpectedReturnDataException(data, "expected IKeyCountQuery");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes) {
		if (ParserUtil.isCoreElement(namespaceUri, name, "sql")) {
			context.pushState(new SqlState(), IKeyCountQuery.class, namespaceUri, name, attributes);
		} else {
			throw new UnexpectedElementException(namespaceUri, name, "expected table query");
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(IParserStateContext context, String namespaceUri, String name) {
		context.popState(chartBlock);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public void consumeText(IParserStateContext context, String text) {
		noTextExpected(text, "expected table query");
	}

}
