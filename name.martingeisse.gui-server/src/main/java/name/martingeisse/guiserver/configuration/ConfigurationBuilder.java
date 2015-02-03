/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentConfigurationList;
import name.martingeisse.guiserver.configuration.content.parser.ContentStreams;
import name.martingeisse.guiserver.configuration.content.parser.RootContentParser;
import name.martingeisse.guiserver.configuration.elements.ConfigurationElement;
import name.martingeisse.guiserver.configuration.elements.ConfigurationElementContent;
import name.martingeisse.guiserver.configuration.elements.FormApiConfiguration;
import name.martingeisse.guiserver.configuration.elements.PageConfiguration;
import name.martingeisse.guiserver.configuration.elements.PanelConfiguration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Helper class to generate the configuration from the configuration files.
 * 
 * Instances of this class should not be used by two callers at the same time.
 */
final class ConfigurationBuilder {

	/**
	 * the elements
	 */
	private final Map<Class<? extends ConfigurationElement>, Map<String, ConfigurationElement>> elements = new HashMap<>();
	
	/**
	 * the pathSegments
	 */
	private final Stack<String> pathSegments = new Stack<>();

	/**
	 * Builds the configuration using the files in the specified root folder and its subfolders.
	 * @param rootFolder the root folder
	 * @return the configuration elements
	 * @throws IOException on I/O errors
	 */
	public Map<Class<? extends ConfigurationElement>, Map<String, ConfigurationElement>> build(File rootFolder) throws IOException {
		elements.clear();
		pathSegments.clear();
		if (!rootFolder.isDirectory()) {
			throw new RuntimeException("root configuration folder " + rootFolder + " doesn't exist or is not a folder");
		}
		handleFolder(rootFolder);
		return ImmutableMap.copyOf(elements);
	}

	/**
	 * 
	 */
	private void handleFolder(File folder) throws IOException {
		for (File element : folder.listFiles()) {
			pathSegments.push(element.getName());
			if (element.isDirectory()) {
				handleFolder(element);
			} else if (element.isFile()) {
				handleFile(element);
			} else {
				throw new RuntimeException("configuration folder contains non-file, non-folder element: " + getPath());
			}
			pathSegments.pop();
		}
	}
	
	/**
	 * 
	 */
	private void handleFile(File file) throws IOException {
		String filename = file.getName();
		if (filename.endsWith(PageConfiguration.CONFIGURATION_FILENAME_SUFFIX)) {
			String path = getPath(PageConfiguration.CONFIGURATION_FILENAME_SUFFIX);
			String wicketMarkup;
			ImmutableList<ComponentConfiguration> components;
			try (FileInputStream fileInputStream = new FileInputStream(file)) {
				ContentStreams streams = new ContentStreams(fileInputStream);
				RootContentParser parser = new RootContentParser(streams);
				parser.parseRootContent();
				wicketMarkup = parser.getStreams().getMarkup();
				components = parser.getStreams().finishRootComponentAccumulator();
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}
			// TODO
			System.out.println("------------------------------------------");
			System.out.println(wicketMarkup);
			System.out.println("------------------------------------------");
			// TODO
			ConfigurationElementContent content = new ConfigurationElementContent(wicketMarkup, new ComponentConfigurationList(components));
			PageConfiguration pageConfiguration = new PageConfiguration(path, content);
			putElement(path, pageConfiguration);
		} else if (filename.endsWith(PanelConfiguration.CONFIGURATION_FILENAME_SUFFIX)) {
			String path = getPath(PanelConfiguration.CONFIGURATION_FILENAME_SUFFIX);
			putElement(path, new PanelConfiguration(path));
		} else if (filename.endsWith(FormApiConfiguration.CONFIGURATION_FILENAME_SUFFIX)) {
			String path = getPath(FormApiConfiguration.CONFIGURATION_FILENAME_SUFFIX);
			putElement(path, new FormApiConfiguration(path));
		} else {
			throw new RuntimeException("ConfigurationBuilder doesn't understand configuration element: " + getPath());
		}
	}

	/**
	 * 
	 */
	private String getPath() {
		if (pathSegments.isEmpty()) {
			return "/";
		}
		StringBuilder builder = new StringBuilder();
		for (String segment : pathSegments) {
			builder.append('/').append(segment);
		}
		return builder.toString();
	}

	/**
	 * 
	 */
	private String getPath(String suffixToRemove) {
		String path = getPath();
		if (!path.endsWith(suffixToRemove)) {
			throw new RuntimeException("configuration file path " + path + " doesn't end with " + suffixToRemove);
		}
		return path.substring(0, path.length() - suffixToRemove.length());
	}

	/**
	 * 
	 */
	private void putElement(String path, ConfigurationElement element) {
		Class<? extends ConfigurationElement> elementClass = element.getClass();
		Map<String, ConfigurationElement> subMap = elements.get(elementClass);
		if (subMap == null) {
			subMap = new HashMap<>();
			elements.put(elementClass, subMap);
		}
		subMap.put(path, element);
	}
	
}
