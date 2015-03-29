/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.element;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.ConfigurationException;
import name.martingeisse.guiserver.configuration.element.xml.DocumentParser;
import name.martingeisse.guiserver.configuration.element.xml.RootDocumentParser;
import name.martingeisse.guiserver.configuration.storage.StorageElement;
import name.martingeisse.guiserver.configuration.storage.StorageException;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.content.ContentParser;

/**
 * Parses configuration XML files.
 */
public class XmlStorageElementParser implements StorageElementParser {

	/**
	 * the documentParser
	 */
	private final DocumentParser documentParser;

	/**
	 * Constructor.
	 * @param templateParser the XML template parser
	 */
	public XmlStorageElementParser(ContentParser<MarkupContent<ComponentGroupConfiguration>> templateParser) {
		this.documentParser = new RootDocumentParser(templateParser);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.StorageElementParser#parse(name.martingeisse.guiserver.configuration.storage.StorageElement, java.lang.String, java.util.List)
	 */
	@Override
	public Element parse(StorageElement element, String path, List<IConfigurationSnippet> snippetAccumulator) throws StorageException, ConfigurationException {
		try (InputStream inputStream = element.open()) {
			MyXmlStreamReader reader = buildXmlReader(inputStream);
			consumeStartDocumentEvent(reader);
			return documentParser.parse(reader, path, snippetAccumulator);
		} catch (XMLStreamException e) {
			throw new ConfigurationException("XML syntax error", e);
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	/**
	 * 
	 */
	private MyXmlStreamReader buildXmlReader(InputStream fileInputStream) throws XMLStreamException {
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
