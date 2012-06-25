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
 * This state can be used in elements for which only text content is allowed.
 * The concrete subclass determines how that text is packaged for the parent
 * state, i.e. they must implement startState() (for type checking) and
 * createReturnData() (to actually create the return value).
 */
public abstract class AbstractTextOnlyState extends AbstractParserState {

	/**
	 * the builder
	 */
	private StringBuilder builder;

	/**
	 * Constructor.
	 */
	public AbstractTextOnlyState() {
		builder = new StringBuilder();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startState(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Class, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public abstract void startState(final IParserStateContext context, final Class<?> expectedReturnType, final String namespaceUri, final String name, final Attributes attributes);

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public final void consumeReturnData(final IParserStateContext context, final Object data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public final void startElement(final IParserStateContext context, final String namespaceUri, final String name, final Attributes attributes) {
		throw new UnexpectedElementException(namespaceUri, namespaceUri, "only text content allowed in this element");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public final void endElement(final IParserStateContext context, final String namespaceUri, final String name) {
		popStateWithAdaptedReturnData(context, createReturnData(builder.toString()));
	}
	
	/**
	 * Creates the actual return value from the text content.
	 * @param text the text content
	 * @return the return data
	 */
	protected abstract Object createReturnData(String text);

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.AbstractParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public final void consumeText(final IParserStateContext context, final String text) {
		builder.append(text);
	}

}
