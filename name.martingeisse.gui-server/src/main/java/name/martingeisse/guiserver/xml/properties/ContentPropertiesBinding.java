/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.properties;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.element.ElementParser;

import org.apache.commons.lang3.StringUtils;

/**
 * Parses and stores content by expecting child elements only and passing
 * each element to a delegate binding.
 */
public final class ContentPropertiesBinding<C, P extends ContentParser<?>> implements PropertiesBinding<C, P> {

	/**
	 * the elementBinding
	 */
	private final PropertiesBinding<C, ? extends ElementParser<?>> elementBinding;

	/**
	 * Constructor.
	 * @param elementBinding the binding to use for each element
	 */
	public ContentPropertiesBinding(PropertiesBinding<C, ElementParser<?>> elementBinding) {
		this.elementBinding = elementBinding;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.properties.PropertiesBinding#bind(name.martingeisse.guiserver.xml.MyXmlStreamReader, java.lang.Object)
	 */
	@Override
	public void bind(MyXmlStreamReader reader, C target) throws XMLStreamException {
		loop: while (true) {
			switch (reader.getEventType()) {

			case XMLStreamConstants.START_ELEMENT:
				if (reader.getNamespaceURI() == null) {
					throw new RuntimeException("special element expected, found <" + reader.getLocalName() + ">");
				} else {
					elementBinding.bind(reader, target);
				}
				reader.skipWhitespace();
				break;

			case XMLStreamConstants.END_ELEMENT:
				break loop;

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.ENTITY_REFERENCE:
				if (!StringUtils.isBlank(reader.getText())) {
					throw new RuntimeException("unexpected text content: " + reader.getText().trim());
				}
				reader.next();
				break;
				
			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.COMMENT:
				reader.next();
				break;
				
			default:
				throw new RuntimeException("invalid XML event while skipping nested content: " + reader.getEventType());

			}
		}
	}

}
