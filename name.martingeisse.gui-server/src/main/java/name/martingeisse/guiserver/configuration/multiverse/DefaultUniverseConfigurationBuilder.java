/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.multiverse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import name.martingeisse.guiserver.configuration.ConfigurationException;
import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.configuration.element.StorageElementParser;
import name.martingeisse.guiserver.configuration.element.StorageElementParserSwitch;
import name.martingeisse.guiserver.configuration.storage.StorageElement;
import name.martingeisse.guiserver.configuration.storage.StorageException;
import name.martingeisse.guiserver.configuration.storage.StorageFolder;
import name.martingeisse.guiserver.configuration.storage.StorageFolderEntry;
import name.martingeisse.guiserver.configuration.storage.UniverseStorage;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.xml.content.ContentParser;

/**
 * Helper class to generate the configuration from the configuration files.
 * 
 * Instances of this class should not be used by two callers at the same time.
 */
final class DefaultUniverseConfigurationBuilder {
	
	/**
	 * the fileParser
	 */
	private final StorageElementParser fileParser;

	/**
	 * the elements
	 */
	private final Map<String, Element> elements = new HashMap<>();

	/**
	 * the pathSegments
	 */
	private final Stack<String> pathSegments = new Stack<>();

	/**
	 * the snippets
	 */
	private final List<IConfigurationSnippet> snippets = new ArrayList<>();

	/**
	 * Constructor.
	 * @param templateParser the XML-content-to-object-binding
	 */
	public DefaultUniverseConfigurationBuilder(ContentParser<MarkupContent<ComponentGroupConfiguration>> templateParser) {
		this.fileParser = new StorageElementParserSwitch(templateParser);
	}

	/**
	 * Builds the configuration using the files in the specified root folder and its subfolders.
	 * @param storage the configuration storage
	 * @param serialNumber the serial number for the configuration to build
	 * @return the configuration elements
	 * @throws StorageException on errors in the storage system
	 */
	public DefaultUniverseConfiguration build(UniverseStorage storage, int serialNumber) throws StorageException, ConfigurationException {
		elements.clear();
		pathSegments.clear();
		snippets.clear();
		handleFolder(storage.getRootFolder());
		return new DefaultUniverseConfiguration(serialNumber, elements, snippets);
	}

	/**
	 * 
	 */
	private void handleFolder(StorageFolder folder) throws StorageException, ConfigurationException {
		for (StorageFolderEntry entry : folder.list()) {
			pathSegments.push(entry.getName());
			if (entry instanceof StorageFolder) {
				handleFolder((StorageFolder)entry);
			} else if (entry instanceof StorageElement) {
				handleElement((StorageElement)entry);
			} else {
				throw new RuntimeException("configuration folder contains non-element, non-folder entry: " + getCurrentPath());
			}
			pathSegments.pop();
		}
	}

	/**
	 * 
	 */
	private void handleElement(StorageElement element) throws StorageException, ConfigurationException {
		String path = getCurrentPath();
		elements.put(path, fileParser.parse(element, path, snippets));
	}

	/**
	 * TODO clean up the vagueness between this method, the folder's configuration path
	 * and the folder's HTTP path
	 */
	private String getCurrentPath() {
		if (pathSegments.isEmpty()) {
			throw new IllegalStateException();
		}
		int remainingPrefixSegments = pathSegments.size() - 1;
		StringBuilder builder = new StringBuilder();
		for (final String segment : pathSegments) {
			if (remainingPrefixSegments > 0) {
				builder.append('/').append(segment);
			} else {
				int dotIndex = segment.indexOf('.');
				if (dotIndex == -1) {
					throw new RuntimeException("invalid configuration element path (missing filename extension): " + segment);
				}
				String baseName = segment.substring(0, dotIndex);
				if (baseName.isEmpty()) {
					throw new RuntimeException("invalid configuration element path (empty base name): " + segment);
				}
				if (baseName.equals("_")) {
					if (pathSegments.size() == 1) {
						builder.append('/');
					}
				} else {
					builder.append('/').append(baseName);
				}
			}
			remainingPrefixSegments--;
		}
		return builder.toString();
	}

}
