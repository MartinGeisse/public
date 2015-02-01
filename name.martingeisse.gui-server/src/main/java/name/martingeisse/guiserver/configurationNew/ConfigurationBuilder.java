/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configurationNew;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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
	 */
	public Map<Class<? extends ConfigurationElement>, Map<String, ConfigurationElement>> build(File rootFolder) {
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
	private void handleFolder(File folder) {
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
	private void handleFile(File file) {
		String filename = file.getName();
		if (filename.endsWith(PageConfiguration.CONFIGURATION_FILENAME_SUFFIX)) {
			String path = getPath(PageConfiguration.CONFIGURATION_FILENAME_SUFFIX);
			putElement(path, new PageConfiguration(path));
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