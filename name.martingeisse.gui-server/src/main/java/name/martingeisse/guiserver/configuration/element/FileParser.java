/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.element;

import java.io.File;
import java.io.IOException;
import java.util.List;

import name.martingeisse.guiserver.configuration.ConfigurationException;
import name.martingeisse.guiserver.template.IConfigurationSnippet;

/**
 * Parses a configuration element definition from its file.
 */
public interface FileParser {

	/**
	 * Parses a configuration element file.
	 * 
	 * @param file the file to parse
	 * @param path the path used to identify the element
	 * @param snippetAccumulator a list that accumulates configuration snippets
	 * @return the configuration element
	 * @throws IOException on I/O errors
	 * @throws ConfigurationException on errors in the configuration
	 */
	public Element parse(File file, String path, List<IConfigurationSnippet> snippetAccumulator) throws IOException, ConfigurationException;
	
}
