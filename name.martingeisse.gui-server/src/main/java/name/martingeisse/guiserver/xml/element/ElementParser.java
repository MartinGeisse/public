/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.element;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.XmlParser;

/**
 * This parser parses an XML element and creates an object of
 * type T from it.
 *
 * @param <T> the type of parsed objects
 */
public interface ElementParser<T> extends XmlParser<T> {

	/**
	 * Parses an object from an XML element and returns it. The stream must
	 * be at the opening tag of the element. This method moves the stream
	 * right after the closing tag of the element.
	 * 
	 * @param reader the XML stream reader
	 * @return the parsed object
	 * @throws XMLStreamException on XML processing errors
	 */
	@Override
	public T parse(MyXmlStreamReader reader) throws XMLStreamException;

}
