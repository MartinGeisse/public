/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.attribute;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.DatabindingXmlStreamReader;

/**
 * This binding parses an XML attribute and creates an
 * object representing its value.
 *
 * @param <T> the type of created values
 */
public interface AttributeValueBinding<T> {

	/**
	 * Parses the attribute from the specified reader. The reader must be located
	 * at a START_ELEMENT. This method won't move the reader.
	 * 
	 * @param reader the reader
	 * @return the parsed attribute value
	 * @throws XMLStreamException on XML processing errors
	 */
	public T parse(DatabindingXmlStreamReader reader) throws XMLStreamException;
	
}
