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
 * the value of an attribute.
 */
public class AttributeSelectedElementParser<T> implements ElementParser<T> {

	/**
	 * the attributeName
	 */
	private String attributeName;

	/**
	 * the parsers
	 */
	private final Map<String, ElementParser<? extends T>> parsers = new HashMap<>();

	/**
	 * Getter method for the attributeName.
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Setter method for the attributeName.
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	/**
	 * Adds a sub-parser to this parser.
	 * 
	 * @param attributeValue the attribute value that selects the specified parser
	 * @param parser the parser to invoke for that attribute value
	 */
	public void addParser(String attributeValue, ElementParser<? extends T> parser) {
		parsers.put(attributeValue, parser);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.element.ElementParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader)
	 */
	@Override
	public final T parse(MyXmlStreamReader reader) throws XMLStreamException {
		String attributeValue = reader.getAttributeValue(null, attributeName);
		if (attributeValue == null) {
			throw new RuntimeException("missing '" + attributeName + "' attribute");
		}
		ElementParser<? extends T> selectedParser = parsers.get(attributeValue);
		if (selectedParser == null) {
			throw new RuntimeException("unknown value for '" + attributeName + "' attribute: " + reader.getLocalName());
		}
		return selectedParser.parse(reader);
	}

}
