/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.configuration.element.FileParser;
import name.martingeisse.guiserver.configuration.element.RootFileParser;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.xml.content.ContentParser;

/**
 * Helper class to generate the configuration from the configuration files.
 * 
 * Instances of this class should not be used by two callers at the same time.
 */
final class Builder {
	
	/**
	 * the fileParser
	 */
	private final FileParser fileParser;

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
	public Builder(File configurationRoot, ContentParser<MarkupContent<ComponentGroupConfiguration>> templateParser) {
		this.fileParser = new RootFileParser(templateParser);
	}

	/**
	 * Builds the configuration using the files in the specified root folder and its subfolders.
	 * @param rootFolder the root folder
	 * @return the configuration elements
	 * @throws IOException on I/O errors
	 */
	public Configuration build(File rootFolder) throws IOException, ConfigurationException {
		elements.clear();
		pathSegments.clear();
		snippets.clear();
		if (!rootFolder.isDirectory()) {
			throw new RuntimeException("root configuration folder " + rootFolder + " doesn't exist or is not a folder");
		}
		handleFolder(rootFolder);
		return new Configuration(elements, snippets);
	}

	/**
	 * 
	 */
	private void handleFolder(File folder) throws IOException, ConfigurationException {
		for (File element : folder.listFiles()) {
			pathSegments.push(element.getName());
			if (element.isDirectory()) {
				handleFolder(element);
			} else if (element.isFile()) {
				handleFile(element);
			} else {
				throw new RuntimeException("configuration folder contains non-file, non-folder element: " + getCurrentPath());
			}
			pathSegments.pop();
		}
	}

	/**
	 * 
	 */
	private void handleFile(File file) throws IOException, ConfigurationException {
		String path = getCurrentPath();
		elements.put(path, fileParser.parse(file, path, snippets));
	}

	/**
	 * 
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
