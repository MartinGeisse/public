/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.attribute;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.XmlParser;

/**
 * This object parses a value from one or more XML attributes.
 * 
 * More formally, this parser is able to obtain a value from an
 * XML stream reader that is at a START_ELEMENT without moving its
 * cursor.
 *
 * @param <T> the type of created values
 */
public interface AttributeParser<T> extends XmlParser<T> {

	/**
	 * Parses the value from the specified reader. The reader must be located
	 * at a START_ELEMENT. This method won't move the reader.
	 * 
	 * @param reader the reader
	 * @return the parsed value
	 * @throws XMLStreamException on XML processing errors
	 */
	@Override
	public T parse(MyXmlStreamReader reader) throws XMLStreamException;
	
}
