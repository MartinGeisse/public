/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.element;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;

/**
 * Base class to implement a simple parser that accepts only empty elements, taking all
 * necessary information from attributes. 
 */
public abstract class AbstractEmptyElementParser<T> implements ElementParser<T> {

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.element.ElementParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader)
	 */
	@Override
	public final T parse(MyXmlStreamReader reader) throws XMLStreamException {
		String elementLocalName = reader.getLocalName();
		T result = createResult(reader);
		reader.next();
		reader.skipWhitespace();
		if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
			throw new RuntimeException("unexpected content in element " + elementLocalName);
		}
		reader.next();
		return result;
	}
	
	/**
	 * Creates the result from the stream reader. The reader is at the START_ELEMENT event when this method
	 * gets called. This method should not move the reader.
	 * 
	 * @param reader the XML stream reader
	 * @return the parser result
	 * @throws XMLStreamException on XML stream processing errors
	 */
	protected abstract T createResult(MyXmlStreamReader reader) throws XMLStreamException;

}
