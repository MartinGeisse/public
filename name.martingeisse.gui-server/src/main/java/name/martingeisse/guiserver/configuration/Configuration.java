/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration;

import java.util.List;
import java.util.Map;

import name.martingeisse.guiserver.application.wicket.GuiWicketApplication;
import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.configuration.storage.SimpleUniverseStorage;
import name.martingeisse.guiserver.configuration.storage.StorageException;
import name.martingeisse.guiserver.configuration.storage.UniverseStorage;
import name.martingeisse.guiserver.configuration.storage.http.ApacheHttpdStorageEngine;
import name.martingeisse.guiserver.configuration.storage.http.HttpFolder;
import name.martingeisse.guiserver.configuration.storage.http.HttpStorageEngine;
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
			
			// TODO remove that option from the server config once everything works
			// File configurationRoot = new File(ServerConfiguration.configurationRoot.getMandatoryValue());
			
			HttpStorageEngine engine = new ApacheHttpdStorageEngine("http://localhost/geisse/demo-gui");
			UniverseStorage storage = new SimpleUniverseStorage(new HttpFolder(engine, "/", "/"));
			Builder builder = new Builder(TemplateParser.INSTANCE);
			instance = builder.build(storage);
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
	private final Map<String, Element> elements;
	
	/**
	 * the snippets
	 */
	private final List<IConfigurationSnippet> snippets;
	
	/**
	 * Constructor.
	 * 
	 * @throws StorageException on errors in the storage system
	 * @throws ConfigurationException on errors in a configuration file
	 */
	public Configuration(Map<String, Element> elements, List<IConfigurationSnippet> snippets) throws StorageException, ConfigurationException {
		this.elements = elements;
		this.snippets = snippets;
	}
	
	/**
	 * Mounts the Wicket URLs needed by this configuration.
	 * 
	 * @param application the Wicket application
	 */
	public void mountWicketUrls(GuiWicketApplication application) {
		for (Element element : elements.values()) {
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
	public <T extends Element> T getElement(Class<T> type, String path) {
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
	public <T extends Element> T getElementOrNull(Class<T> type, String path) {
		Element element = getElement(path);
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
	public Element getElement(String path) {
		Element element = getElementOrNull(path);
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
	public Element getElementOrNull(String path) {
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
