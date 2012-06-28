/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.states;

import name.martingeisse.reporting.parser.IParserStateContext;
import name.martingeisse.reporting.parser.UnexpectedElementException;

import org.xml.sax.Attributes;

/**
 * This state does not expect any sub-elements or text content, accepts any
 * return type but returns null.
 */
public final class EmptyState extends AbstractParserState {

	/**
	 * Constructor.
	 */
	public EmptyState() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startState(final IParserStateContext context, final Class<?> expectedReturnType, final String namespaceUri, final String name, final Attributes attributes) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public void consumeReturnData(final IParserStateContext context, final Object data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(final IParserStateContext context, final String namespaceUri, final String name, final Attributes attributes) {
		throw new UnexpectedElementException(namespaceUri, namespaceUri, "no content allowed in this element");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public final void endElement(final IParserStateContext context, final String namespaceUri, final String name) {
		context.popState(null);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public final void consumeText(final IParserStateContext context, final String text) {
		noTextExpected(text, "no content allowed in this element");
	}

}
