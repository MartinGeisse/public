/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.element;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;

/**
 * This element parser selects one of several parsers based on
 * the element name.
 */
public class NameSelectedElementParser<T> implements ElementParser<T> {

	/**
	 * the parsers
	 */
	private final Map<String, ElementParser<? extends T>> parsers = new HashMap<String, ElementParser<? extends T>>();

	/**
	 * Adds a sub-parser to this parser.
	 * 
	 * @param localElementName the local element name that selects the specified parser
	 * @param parser the parser to invoke for that element name
	 */
	public void addParser(String localElementName, ElementParser<? extends T> parser) {
		parsers.put(localElementName, parser);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.element.ElementParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader)
	 */
	@Override
	public final T parse(MyXmlStreamReader reader) throws XMLStreamException {
		ElementParser<? extends T> selectedParser = parsers.get(reader.getLocalName());
		if (selectedParser == null) {
			throw new RuntimeException("unknown special element: " + reader.getLocalName());
		}
		return selectedParser.parse(reader);
	}

}
