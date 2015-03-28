/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.element.xml;

import java.util.List;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.ConfigurationException;
import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.content.ContentParser;

/**
 * Used as the entry point for parsing XML-based configuration documents.
 * 
 * This parser inspects the document element name to select a sub-parser.
 */
public class RootDocumentParser implements DocumentParser {

	/**
	 * the pageParser
	 */
	private final PageParser pageParser;
	
	/**
	 * the panelParser
	 */
	private final PanelParser panelParser;

	/**
	 * Constructor.
	 * @param templateParser the template parser
	 */
	public RootDocumentParser(ContentParser<MarkupContent<ComponentGroupConfiguration>> templateParser) {
		this.pageParser = new PageParser(templateParser);
		this.panelParser = new PanelParser(templateParser);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.xml.DocumentParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader, java.lang.String, java.util.List)
	 */
	@Override
	public Element parse(MyXmlStreamReader reader, String path, List<IConfigurationSnippet> snippetAccumulator) throws XMLStreamException, ConfigurationException {
		if (reader.getNamespaceURI() == null) {
			throw new ConfigurationException("invalid document element -- expected special element");
		}
		String localName = reader.getLocalName();
		if (localName.equals("page")) {
			return pageParser.parse(reader, path, snippetAccumulator);
		} else if (localName.equals("panel")) {
			return panelParser.parse(reader, path, snippetAccumulator);
		} else {
			throw new ConfigurationException("invalid document element -- unknown configuration element type: " + localName);
		}
	}

}
