/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml;

import javax.xml.stream.XMLStreamException;

/**
 * Base class for XML parsers of various types. This interface specifies
 * the signature of the parsing method, but not its semantics with
 * respect to the state of the XML reader.
 */
public interface XmlParser<T> {

	/**
	 * Parses an object from an XML stream reader and returns it.
	 * 
	 * Subtypes of this interface specify the semantics with respect
	 * to the state of the reader.
	 * 
	 * @param reader the XML stream reader
	 * @return the parsed object
	 * @throws XMLStreamException on XML processing errors
	 */
	public T parse(MyXmlStreamReader reader) throws XMLStreamException;

}
