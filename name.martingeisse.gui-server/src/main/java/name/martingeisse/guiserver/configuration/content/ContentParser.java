/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.google.common.collect.ImmutableList;

/**
 * TODO: document me
 *
 */
public final class ContentParser {

	/**
	 * the GUI_NAMESPACE_URI
	 */
	public static final String GUI_NAMESPACE_URI = "http://guiserver.martingeisse.name/v1";

	/**
	 * the reader
	 */
	private final XMLStreamReader reader;

	/**
	 * the writer
	 */
	private final XMLStreamWriter writer;

	/**
	 * the componentAccumulator
	 */
	private List<ComponentConfiguration> componentAccumulator;
	
	/**
	 * the componentCount
	 */
	private int componentCount;

	/**
	 * Constructor.
	 * @param reader the reader
	 * @param writer the writer
	 * @param componentAccumulator a list that accumulates toplevel components
	 */
	public ContentParser(XMLStreamReader reader, XMLStreamWriter writer, List<ComponentConfiguration> componentAccumulator) {
		this.reader = reader;
		this.writer = writer;
		this.componentAccumulator = componentAccumulator;
		this.componentCount = 0;
	}

	/**
	 * Parses the content.
	 */
	public void parse() throws XMLStreamException {

		// handle document start
		if (reader.getEventType() != XMLStreamConstants.START_DOCUMENT) {
			throw new IllegalStateException("not at document start");
		}
		reader.next();
		if (reader.getEventType() != XMLStreamConstants.START_ELEMENT) {
			throw new RuntimeException("missing page element");
		}
		if (!reader.getLocalName().equals("page") || !GUI_NAMESPACE_URI.equals(reader.getNamespaceURI())) {
			throw new RuntimeException("invalid page element: " + reader.getNamespaceURI() + " / " + reader.getLocalName());
		}
		reader.next();
		writer.writeStartDocument();
		writer.writeStartElement("html");
		writer.writeStartElement("body");

		// handle content
		handleNestedContent();

		// handle document end
		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndDocument();

	}

	/**
	 * Handles content until a closing tag is encountered to which the start tag has already
	 * been handled.
	 */
	private void handleNestedContent() throws XMLStreamException {
		int nesting = 0;
		loop: while (true) {
			switch (reader.getEventType()) {

			case XMLStreamConstants.START_ELEMENT:
				if (GUI_NAMESPACE_URI.equals(reader.getNamespaceURI())) {
					handleSpecialElement();
				} else {
					writer.writeStartElement(reader.getLocalName());
					int count = reader.getAttributeCount();
					for (int i = 0; i < count; i++) {
						if (reader.getAttributeNamespace(i) == null) {
							writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
						} else {
							writer.writeAttribute(reader.getAttributeNamespace(i), reader.getAttributeLocalName(i), reader.getAttributeValue(i));
						}
					}
					reader.next();
					nesting++;
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				if (nesting > 0) {
					writer.writeEndElement();
					reader.next();
					nesting--;
					break;
				} else {
					break loop;
				}

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.ENTITY_REFERENCE:
				writer.writeCharacters(reader.getText());
				reader.next();
				break;

			default:
				throw new RuntimeException("invalid XML event: " + reader.getEventType());

			}
		}
	}

	/**
	 * 
	 */
	private void handleSpecialElement() throws XMLStreamException {
		switch (reader.getLocalName()) {

		case "link": {
			String path = reader.getAttributeValue(null, "href");
			if (path == null) {
				throw new RuntimeException("link specialtag without href attribute");
			}
			String componentId = "link" + componentCount;
			componentCount++;
			writer.writeStartElement("a");
			writer.writeAttribute("wicket:id", componentId);
			reader.next();
			componentAccumulator.add(new LinkConfiguration(componentId, handleComponentContent(), path));
			writer.writeEndElement();
			reader.next();
			break;
		}

		case "navbar": {
			String componentId = "navbar" + componentCount;
			componentCount++;
			writer.writeStartElement("div");
			writer.writeAttribute("wicket:id", componentId);
			reader.next();
			
			LinkConfiguration brandLink = null;
//			if (reader.getLocalName().equals("brandLink")) {
//				reader.next();
//				// parse brand link
//			}
			componentAccumulator.add(new NavigationBarConfiguration(componentId, handleComponentContent(), brandLink));
			
			writer.writeEndElement();
			reader.next();
			break;
		}

		default:
			throw new RuntimeException("unknown special tag: " + reader.getLocalName());

		}
	}
	
	private ImmutableList<ComponentConfiguration> handleComponentContent() throws XMLStreamException {
		List<ComponentConfiguration> oldComponentAccumulator = componentAccumulator;
		List<ComponentConfiguration> newComponentAccumulator = componentAccumulator = new ArrayList<>();
		handleNestedContent();
		componentAccumulator = oldComponentAccumulator;
		return ImmutableList.copyOf(newComponentAccumulator);
	}

}
