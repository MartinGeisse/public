/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.xml.ContentStreams;

/**
 * This parser is used on top level to parse a content XML file.
 */
public final class RootContentParser extends DefaultContentParser {

	/**
	 * the shared instance of this class
	 */
	public static final RootContentParser ROOT_PARSER_INSTANCE = new RootContentParser();

	/**
	 * Parses the root content.
	 * 
	 * @param streams the content streams
	 * @throws XMLStreamException on XML processing errors
	 */
	public void parseRootContent(ContentStreams streams) throws XMLStreamException {
		XMLStreamWriter writer = streams.getWriter();

		// handle document start
		streams.expectSpecialDocumentElement("page");
		writer.writeStartDocument();
		writer.writeStartElement("html");
		writer.writeStartElement("body");

		// handle content
		parse(streams);

		// handle document end
		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndDocument();

	}

}
