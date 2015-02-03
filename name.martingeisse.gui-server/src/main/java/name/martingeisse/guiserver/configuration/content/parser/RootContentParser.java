/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * This parser is used on top level to parse a content XML file.
 */
public final class RootContentParser extends DefaultContentParser {

	/**
	 * Constructor.
	 * @param streams the content streams
	 */
	public RootContentParser(ContentStreams streams) {
		super(streams);
	}

	/**
	 * Parses the root content.
	 */
	public void parseRootContent() throws XMLStreamException {
		ContentStreams streams = getStreams();
		XMLStreamWriter writer = streams.getWriter();

		// handle document start
		streams.expectSpecialDocumentElement("page");
		writer.writeStartDocument();
		writer.writeStartElement("html");
		writer.writeStartElement("body");

		// handle content
		parseNestedContent();

		// handle document end
		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndDocument();

	}

}
