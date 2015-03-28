/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.element;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.ConfigurationException;
import name.martingeisse.guiserver.configuration.element.xml.DocumentParser;
import name.martingeisse.guiserver.configuration.element.xml.RootDocumentParser;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.content.ContentParser;

/**
 * Parses configuration XML files.
 */
public class XmlFileParser implements FileParser {

	/**
	 * the documentParser
	 */
	private final DocumentParser documentParser;

	/**
	 * Constructor.
	 * @param templateParser the XML template parser
	 */
	public XmlFileParser(ContentParser<MarkupContent<ComponentGroupConfiguration>> templateParser) {
		this.documentParser = new RootDocumentParser(templateParser);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.FileParser#parse(java.io.File, java.lang.String, java.util.List)
	 */
	@Override
	public Element parse(File file, String path, List<IConfigurationSnippet> snippetAccumulator) throws IOException, ConfigurationException {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			MyXmlStreamReader reader = buildXmlReader(fileInputStream);
			consumeStartDocumentEvent(reader);
			return documentParser.parse(reader, path, snippetAccumulator);
		} catch (XMLStreamException e) {
			throw new ConfigurationException("XML syntax error", e);
		}
	}

	/**
	 * 
	 */
	private MyXmlStreamReader buildXmlReader(FileInputStream fileInputStream) throws XMLStreamException {
		return new MyXmlStreamReader(XMLInputFactory.newFactory().createXMLStreamReader(fileInputStream));
	}

	/**
	 * 
	 */
	private void consumeStartDocumentEvent(MyXmlStreamReader reader) throws XMLStreamException {
		if (reader.getEventType() != XMLStreamConstants.START_DOCUMENT) {
			throw new IllegalStateException("reader is not at the START_DOCUMENT event");
		}
		reader.next();
	}

}
