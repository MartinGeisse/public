/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.element;

import java.io.File;
import java.io.IOException;
import java.util.List;

import name.martingeisse.guiserver.configuration.ConfigurationException;
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
public class RootFileParser implements FileParser {

	/**
	 * the xmlParser
	 */
	private final XmlFileParser xmlParser;

	/**
	 * Constructor.
	 * @param templateParser the XML template parser
	 */
	public RootFileParser(ContentParser<MarkupContent<ComponentGroupConfiguration>> templateParser) {
		this.xmlParser = new XmlFileParser(templateParser);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.FileParser#parse(java.io.File, java.lang.String, java.util.List)
	 */
	@Override
	public Element parse(File file, String path, List<IConfigurationSnippet> snippetAccumulator) throws IOException, ConfigurationException {
		String extension = FilenameUtils.getExtension(file.getName());
		if (extension.equals("xml")) {
			return xmlParser.parse(file, path, snippetAccumulator);
		} else {
			throw new ConfigurationException("unknown configuration file name extension '" + extension + "' for file " + file.getAbsolutePath());
		}
	}

}
