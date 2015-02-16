/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.element;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.DatabindingXmlStreamReader;

/**
 * This binding parses an XML element and creates an object of
 * type T from it.
 *
 * @param <T> the type of parsed objects
 */
public interface ElementObjectBinding<T> {

	/**
	 * Parses an object from an XML element and returns it. The stream must
	 * be at the opening tag of the element. This method moves the stream
	 * right after the closing tag of the element.
	 * 
	 * @param reader the XML stream reader
	 * @return the parsed object
	 * @throws XMLStreamException on XML processing errors
	 */
	public T parse(DatabindingXmlStreamReader reader) throws XMLStreamException;

}
