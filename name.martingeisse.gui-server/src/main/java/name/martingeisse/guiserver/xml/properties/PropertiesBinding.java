/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.properties;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.XmlParser;

/**
 * Represents an object that parses one or more properties of a
 * container object and stores them in that object.
 * 
 * The semantics of this class with respect to the state of the XML stream
 * reader is the same as that of the parser type. This class is
 * parameterized with the parser type to allow specifying those semantics
 * through the static type.
 *
 * @param <C> the type of the container object
 * @param <P> the parser type
 */
@SuppressWarnings("unused")
public interface PropertiesBinding<C, P extends XmlParser<?>> {

	/**
	 * Parses XML according to the semantics of the parser type and
	 * stores the information in properties of the target object.
	 * 
	 * @param reader the XML stream reader
	 * @param target the target object to invoke the method on
	 * @throws XMLStreamException on XML processing errors
	 */
	public void bind(MyXmlStreamReader reader, C target) throws XMLStreamException;
	
}
