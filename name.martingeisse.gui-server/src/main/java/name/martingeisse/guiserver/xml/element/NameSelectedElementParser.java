/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.element;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;

import com.google.common.collect.ImmutableMap;

/**
 * This element parser selects one of several parsers based on
 * the element name.
 */
public class NameSelectedElementParser<T> implements ElementParser<T> {

	/**
	 * the parsers
	 */
	private final Map<String, ElementParser<? extends T>> parsers;

	/**
	 * Constructor.
	 * @param parsers the parsers
	 */
	public NameSelectedElementParser(Map<String, ElementParser<? extends T>> parsers) {
		this.parsers = ImmutableMap.copyOf(parsers);
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
