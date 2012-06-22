/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import org.xml.sax.Attributes;

/**
 * The report definition parser keeps a stack of instances of this interface.
 * The top-of-stack is the current state. The parser simply forwards SAX
 * events to the current state.
 */
public interface IParserState {

	/**
	 * This method is invoked when this state is pushed on the context stack.
	 * @param context the context
	 */
	public void startState(IParserStateContext context);

	/**
	 * This method is invoked when this state becomes top-of-stack due to the
	 * next higher level state being pop'ed off the stack.
	 * @param context the context
	 * @param data the data returned by the pop'ed state
	 */
	public void consumeReturnData(IParserStateContext context, Object data);
	
	/**
	 * Consumes the start tag of an element
	 * @param context the context
	 * @param namespaceUri the namespace URI of the element
	 * @param name the name of the element
	 * @param attributes the attributes of the element
	 */
	public void startElement(IParserStateContext context, String namespaceUri, String name, Attributes attributes);

	/**
	 * Consumes the end tag of an element
	 * @param context the context
	 * @param namespaceUri the namespace URI of the element
	 * @param name the name of the element
	 */
	public void endElement(IParserStateContext context, String namespaceUri, String name);

	/**
	 * Consumes a chunk of text. TODO: make the parser concatenate maximal chunks of text.
	 * Chunks in report definition documents are short enough to be buffered and consumed at once.
	 * @param context the context
	 * @param text the text to consume
	 */
	public void consumeText(IParserStateContext context, String text);
	
}
