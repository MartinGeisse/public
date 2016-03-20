/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.element;

import java.util.List;

import name.martingeisse.guiserver.configuration.ConfigurationException;
import name.martingeisse.guiserver.configuration.storage.StorageElement;
import name.martingeisse.guiserver.configuration.storage.StorageException;
import name.martingeisse.guiserver.template.IConfigurationSnippet;

/**
 * Parses a configuration element definition from its file.
 */
public interface StorageElementParser {

	/**
	 * Parses a configuration element file.
	 * 
	 * @param element the storage element to parse
	 * @param path the path used to identify the element
	 * @param snippetAccumulator a list that accumulates configuration snippets
	 * @return the configuration element
	 * @throws StorageException on storage errors
	 * @throws ConfigurationException on errors in the configuration
	 */
	public Element parse(StorageElement element, String path, List<IConfigurationSnippet> snippetAccumulator) throws StorageException, ConfigurationException;
	
}
