/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.element;

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
	private final String attributeName;

	/**
	 * the parsers
	 */
	private final Map<String, ElementParser<? extends T>> parsers;

	/**
	 * Constructor.
	 * @param attributeName the name of the attribute
	 * @param parsers the parsers
	 */
	public AttributeSelectedElementParser(String attributeName, Map<String, ElementParser<? extends T>> parsers) {
		this.attributeName = attributeName;
		this.parsers = parsers;
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
