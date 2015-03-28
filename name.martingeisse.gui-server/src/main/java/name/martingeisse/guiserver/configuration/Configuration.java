/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import name.martingeisse.guiserver.application.ServerConfiguration;
import name.martingeisse.guiserver.application.wicket.GuiWicketApplication;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.TemplateParser;

/**
 * This singleton class provides the data from the configuration.
 */
public final class Configuration {

	/**
	 * the instance
	 */
	private static final Configuration instance;
	
	//
	static {
		try {
			File configurationRoot = new File(ServerConfiguration.configurationRoot.getMandatoryValue());
			Builder builder = new Builder(configurationRoot, TemplateParser.INSTANCE);
			instance = builder.build(configurationRoot);
		} catch (Exception e) {
			throw new RuntimeException("could not load configuration", e);
		}
	}
	
	/**
	 * Getter method for the instance.
	 * @return the instance
	 */
	public static Configuration getInstance() {
		return instance;
	}
		
	/**
	 * the elements
	 */
	private final Map<String, ConfigurationElement> elements;
	
	/**
	 * the snippets
	 */
	private final List<IConfigurationSnippet> snippets;
	
	/**
	 * Constructor.
	 * 
	 * @throws IOException on I/O errors
	 * @throws ConfigurationException on errors in a configuration file
	 */
	public Configuration(Map<String, ConfigurationElement> elements, List<IConfigurationSnippet> snippets) throws IOException, ConfigurationException {
		this.elements = elements;
		this.snippets = snippets;
	}
	
	/**
	 * Mounts the Wicket URLs needed by this configuration.
	 * 
	 * @param application the Wicket application
	 */
	public void mountWicketUrls(GuiWicketApplication application) {
		for (ConfigurationElement element : elements.values()) {
			element.mountWicketUrls(application);
		}
	}

	/**
	 * Obtains a specific configuration element. Throws an exception if the element
	 * doesn't exist.
	 * 
	 * @param type the type of configuration element to obtain
	 * @param path the path to the element
	 * @return the configuration element
	 */
	public <T extends ConfigurationElement> T getElement(Class<T> type, String path) {
		T element = getElementOrNull(type, path);
		if (element == null) {
			throw new RuntimeException("configuration element with type " + type + " and path " + path + " doesn't exist");
		}
		return element;
	}

	/**
	 * Obtains a specific configuration element. Returns null if the element doesn't exist.
	 * 
	 * @param type the type of configuration element to obtain
	 * @param path the path to the element
	 * @return the configuration element
	 */
	public <T extends ConfigurationElement> T getElementOrNull(Class<T> type, String path) {
		ConfigurationElement element = getElement(path);
		return (type.isInstance(element) ? type.cast(element) : null);
	}

	/**
	 * Obtains a specific configuration element. Throws an exception if the element
	 * doesn't exist.
	 * 
	 * @param type the type of configuration element to obtain
	 * @param path the path to the element
	 * @return the configuration element
	 */
	public ConfigurationElement getElement(String path) {
		ConfigurationElement element = getElementOrNull(path);
		if (element == null) {
			throw new RuntimeException("configuration element with path " + path + " doesn't exist");
		}
		return element;
	}
	
	/**
	 * Obtains a specific configuration element. Returns null if the element doesn't exist.
	 * 
	 * @param type the type of configuration element to obtain
	 * @param path the path to the element
	 * @return the configuration element
	 */
	public ConfigurationElement getElementOrNull(String path) {
		return elements.get(path);
	}
	
	/**
	 * Obtains a configuration snippet.
	 * 
	 * @param handle the snippet handle
	 * @return the snippet
	 */
	public IConfigurationSnippet getSnippet(int handle) {
		return snippets.get(handle);
	}
	
}
