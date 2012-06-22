/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import java.util.Stack;

import name.martingeisse.reporting.document.Document;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * The implementation of {@link IParserStateContext}.
 */
class ParserStateContext extends DefaultHandler2 implements IParserStateContext {

	/**
	 * the stateStack
	 */
	private final Stack<IParserState> stateStack;
	
	/**
	 * the document
	 */
	private Document document;
	
	/**
	 * Constructor.
	 */
	public ParserStateContext() {
		this.stateStack = new Stack<IParserState>();
		this.document = null;
	}

	/**
	 * Getter method for the stateStack.
	 * @return the stateStack
	 */
	public Stack<IParserState> getStateStack() {
		return stateStack;
	}

	/**
	 * Getter method for the document.
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		RootState state = new RootState();
		this.document = state.getDocument();
		stateStack.clear();
		pushState(state);
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		stateStack.clear();
	} 

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserStateContext#pushState(name.martingeisse.reporting.parser.IParserState)
	 */
	@Override
	public void pushState(IParserState state) {
		stateStack.push(state);
		state.startState(this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.IParserStateContext#popState(java.lang.Object)
	 */
	@Override
	public void popState(Object returnData) {
		stateStack.pop();
		stateStack.peek().consumeReturnData(this, returnData);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String namespaceUri, String localName, String qualifiedName, Attributes attributes) throws SAXException {
		stateStack.peek().startElement(this, namespaceUri, localName, attributes);
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String namespaceUri, String localName, String qualifiedName) throws SAXException {
		stateStack.peek().endElement(this, namespaceUri, localName);
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] buffer, int start, int length) throws SAXException {
		stateStack.peek().consumeText(this, new String(buffer, start, length));
	}
	
}
