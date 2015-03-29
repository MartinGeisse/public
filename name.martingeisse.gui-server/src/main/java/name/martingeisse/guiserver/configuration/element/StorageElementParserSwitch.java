/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.element;

import java.util.List;

import name.martingeisse.guiserver.configuration.ConfigurationException;
import name.martingeisse.guiserver.configuration.storage.StorageElement;
import name.martingeisse.guiserver.configuration.storage.StorageException;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.xml.content.ContentParser;

import org.apache.commons.io.FilenameUtils;

/**
 * Used as the entry point for parsing configuration element files.
 * 
 * This parser inspects the filename extension to select a sub-parser.
 */
public class StorageElementParserSwitch implements StorageElementParser {

	/**
	 * the xmlParser
	 */
	private final XmlStorageElementParser xmlParser;

	/**
	 * Constructor.
	 * @param templateParser the XML template parser
	 */
	public StorageElementParserSwitch(ContentParser<MarkupContent<ComponentGroupConfiguration>> templateParser) {
		this.xmlParser = new XmlStorageElementParser(templateParser);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.StorageElementParser#parse(name.martingeisse.guiserver.configuration.storage.StorageElement, java.lang.String, java.util.List)
	 */
	@Override
	public Element parse(StorageElement element, String path, List<IConfigurationSnippet> snippetAccumulator) throws StorageException, ConfigurationException {
		String extension = FilenameUtils.getExtension(element.getName());
		if (extension.equals("xml")) {
			return xmlParser.parse(element, path, snippetAccumulator);
		} else {
			throw new ConfigurationException("unknown configuration file name extension '" + extension + "' for configuration element " + element.getPath());
		}
	}

}
