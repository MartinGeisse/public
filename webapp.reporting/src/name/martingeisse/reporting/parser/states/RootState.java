/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.states;

import name.martingeisse.reporting.document.Document;
import name.martingeisse.reporting.parser.IParserStateContext;
import name.martingeisse.reporting.parser.ParserUtil;
import name.martingeisse.reporting.parser.UnexpectedElementException;

import org.xml.sax.Attributes;

/**
 * This state is the first state on the state stack of a report
 * definition parser.
 */
public class RootState extends AbstractParserState {

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
	 * @see name.martingeisse.reporting.parser.IParserState#startElement(name.martingeisse.reporting.parser.IParserStateContext, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes) {
		if (ParserUtil.isCoreElement(namespaceUri, name, "report")) {
			context.pushState(new DocumentState(document), null, namespaceUri, name, attributes);
		} else {
			throw new UnexpectedElementException(namespaceUri, name, "expected <core:report>");
		}
	}

}
