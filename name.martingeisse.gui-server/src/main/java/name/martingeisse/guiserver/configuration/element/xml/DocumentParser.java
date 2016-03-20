/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.element.xml;

import java.util.List;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.ConfigurationException;
import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;

/**
 * Base type for parsing XML documents.
 */
public interface DocumentParser {

	/**
	 * Parses a configuration XML document.
	 * 
	 * The reader must be positioned at the START_ELEMENT of the document
	 * element, i.e. the START_DOCUMENT must have been skipped already. This
	 * rule is necessary to allow cascading parsers in a decorator pattern.
	 * 
	 * @param reader the XML stream reader
	 * @param path the path used to identify the element
	 * @param snippetAccumulator a list that accumulates configuration snippets
	 * @return the configuration element
	 * @throws XMLStreamException on XML processing errors
	 * @throws ConfigurationException on errors in the configuration
	 */
	public Element parse(MyXmlStreamReader reader, String path, List<IConfigurationSnippet> snippetAccumulator) throws XMLStreamException, ConfigurationException;

}
