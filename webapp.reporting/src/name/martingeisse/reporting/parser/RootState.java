/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import name.martingeisse.reporting.document.Document;

import org.xml.sax.Attributes;

/**
 * This state is the first state on the state stack of a report
 * definition parser.
 */
public class RootState implements IParserState {

	/**
	 * the document
	 */
	private final Document document;
	
	/**
	 * Constructor.
	 */
	public RootState() {
		this.document = new Document();
	}

	/**
	 * Getter method for the document.
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startState(name.martingeisse.reporting.parser.IParserStateContext)
	 */
	@Override
	public void startState(IParserStateContext context) {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#consumeReturnData(name.martingeisse.reporting.parser.IParserStateContext, java.lang.Object)
	 */
	@Override
	public void consumeReturnData(IParserStateContext context, Object data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes) {
		if (ParserUtil.isCoreElement(namespaceUri, name, "report")) {
			context.pushState(new DocumentState(document));
		} else {
			throw new ParserException("unexpected root element, expected 'core:report', found [" + namespaceUri + "]:" + name);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#endElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(IParserStateContext context, String namespaceUri, String name) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserState#consumeText(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String)
	 */
	@Override
	public void consumeText(IParserStateContext context, String text) {
	}

}
