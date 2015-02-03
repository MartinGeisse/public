/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.LinkConfiguration;
import name.martingeisse.guiserver.configuration.content.NavigationBarConfiguration;

import com.google.common.collect.ImmutableList;

/**
 * This parser is used on top level to parse a content XML file.
 */
public class DefaultContentParser extends ContentParser {

	/**
	 * Constructor.
	 * 
	 * @param streams the content streams
	 */
	public DefaultContentParser(ContentStreams streams) {
		super(streams);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.parser.ContentParser#handleSpecialElement(java.lang.String)
	 */
	@Override
	protected void handleSpecialElement(String localName) throws XMLStreamException {
		ContentStreams streams = getStreams();
		XMLStreamWriter writer = streams.getWriter();
		XMLStreamReader reader = streams.getReader();
		switch (localName) {

		case "link": {
			String href = streams.getMandatoryAttribute("href");
			String componentId = newComponentId("link");
			writer.writeStartElement("a");
			writer.writeAttribute("wicket:id", componentId);
			reader.next();
			streams.addComponent(new LinkConfiguration(componentId, parseComponentContent(), href));
			writer.writeEndElement();
			reader.next();
			break;
		}

		case "navbar": {
			String componentId = newComponentId("navbar");
			writer.writeStartElement("div");
			writer.writeAttribute("wicket:id", componentId);
			streams.next();
			streams.skipWhitespace();
			
			ComponentConfiguration brandLink = null;
			String brandLinkElementLocalName = streams.recognizeStartSpecialElement();
			if (brandLinkElementLocalName != null && brandLinkElementLocalName.equals("brandLink")) {
				streams.next();
				streams.skipWhitespace();
				String linkElementLocalName = streams.recognizeStartSpecialElement();
				if (linkElementLocalName == null) {
					throw new RuntimeException("link-related special tag expected inside brandLink element");
				}
				streams.beginComponentAccumulator();
				handleSpecialElement(linkElementLocalName);
				ImmutableList<ComponentConfiguration> brandLinkList = streams.finishComponentAccumulator();
				if (brandLinkList.size() != 1) {
					throw new RuntimeException("expected exactly 1 brandLink");
				}
				brandLink = brandLinkList.get(0);
				streams.skipWhitespace();
				streams.next();
			}
			streams.addComponent(new NavigationBarConfiguration(componentId, parseComponentContent(), brandLink));
			
			writer.writeEndElement();
			reader.next();
			break;
		}

		default:
			throw new RuntimeException("unknown special tag: " + reader.getLocalName());

		}
	}
	
}
