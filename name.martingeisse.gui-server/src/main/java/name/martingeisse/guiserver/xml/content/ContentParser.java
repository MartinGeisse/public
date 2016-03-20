/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.content;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.XmlParser;

/**
 * This binding parses properly nested XML content and creates an object of
 * type T from it.
 * 
 * "Properly nested" is this context means that this binding won't consume
 * a closing tag for which it did not consume the opening tag.
 *
 * @param <T> the type of parsed objects
 */
public interface ContentParser<T> extends XmlParser<T> {

	/**
	 * Parses an object from properly nested XML content and returns it.
	 * This moves the XML stream reader to the end of the nested content,
	 * which is the first closing tag for which it did not see the
	 * opening tag.
	 * 
	 * @param reader the XML stream reader
	 * @return the parsed object
	 * @throws XMLStreamException on XML processing errors
	 */
	@Override
	public T parse(MyXmlStreamReader reader) throws XMLStreamException;

}
