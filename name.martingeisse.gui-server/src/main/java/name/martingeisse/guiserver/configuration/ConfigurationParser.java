/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.guiserver.configuration.content.ContentElementConfiguration;
import name.martingeisse.guiserver.configuration.content.HtmlContentConfiguration;
import name.martingeisse.guiserver.configuration.content.NavigationBarContentConfiguration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Parses the configuration JSON.
 */
final class ConfigurationParser {

	/**
	 * Prevent instantiation.
	 */
	private ConfigurationParser() {
	}

	/**
	 * Parses a configuration file.
	 * 
	 * @param file the file to parse
	 * @return the root namespace
	 * @throws IOException on I/O errors
	 * @throws ConfigurationException on errors in a configuration file
	 */
	static ConfigurationNamespace parse(File file) throws IOException, ConfigurationException {
		try (FileInputStream stream = new FileInputStream(file)) {
			try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
				return parseNamespace(JsonAnalyzer.parse(reader));
			}
		}
	}

	/**
	 * Parses a configuration namespace from its JSON specification.
	 * 
	 * @param json the JSON specification
	 * @return the namespace
	 * @throws ConfigurationException on errors in a configuration file
	 */
	static ConfigurationNamespace parseNamespace(JsonAnalyzer json) throws ConfigurationException {
		Map<String, ConfigurationElement> elements = new HashMap<>();
		String defaultPageId = null;
		for (Map.Entry<String, JsonAnalyzer> namespaceProperty : json.analyzeMap().entrySet()) {
			String key = namespaceProperty.getKey();
			JsonAnalyzer value = namespaceProperty.getValue();
			switch (key) {

			case "elements": {
				for (Map.Entry<String, JsonAnalyzer> element : value.analyzeMap().entrySet()) {
					elements.put(element.getKey(), parseConfigurationElement(element.getValue()));
				}
				break;
			}

			case "defaultPageId": {
				defaultPageId = value.tryString();
				break;
			}

			default:
				throw new ConfigurationException("invalid property key for namespace: " + key);

			}
		}
		return new ConfigurationNamespace(ImmutableMap.copyOf(elements), defaultPageId);
	}

	/**
	 * Parses a configuration element from its JSON specification.
	 * 
	 * @param json the JSON specification
	 * @return the element
	 * @throws ConfigurationException on errors in a configuration file
	 */
	static ConfigurationElement parseConfigurationElement(JsonAnalyzer json) throws ConfigurationException {
		String type = json.analyzeMapElement("type").expectString();
		switch (type) {

		case "namespace":
			return parseNamespace(json);

		case "page":
			return parsePageConfiguration(json);

		default:
			throw new ConfigurationException("unknown configuration element type: " + type);

		}
	}

	/**
	 * Parses a page configuration.
	 * 
	 * @param json the JSON specification
	 * @return the page configuration
	 * @throws ConfigurationException on errors in a configuration file
	 */
	static PageConfiguration parsePageConfiguration(JsonAnalyzer json) throws ConfigurationException {
		String urlPath = json.analyzeMapElement("url").expectString();
		if (urlPath.startsWith("/")) {
			urlPath = urlPath.substring(1);
		}
		List<ContentElementConfiguration> contentElements = new ArrayList<>();
		for (JsonAnalyzer element : json.analyzeMapElement("contentElements").analyzeList()) {
			contentElements.add(parseContentElement(element));
		}
		return new PageConfiguration(urlPath, ImmutableList.copyOf(contentElements));
	}

	/**
	 * Parses a content element from its JSON specification.
	 * 
	 * @param json the JSON specification
	 * @return the element
	 * @throws ConfigurationException on errors in a configuration file
	 */
	static ContentElementConfiguration parseContentElement(JsonAnalyzer json) throws ConfigurationException {
		String type = json.analyzeMapElement("type").expectString();
		switch (type) {

		case "html":
			return new HtmlContentConfiguration(json.analyzeMapElement("html").expectString());

		case "navbar":
			return new NavigationBarContentConfiguration();

		default:
			throw new ConfigurationException("unknown content element type: " + type);

		}
	}
	
}
