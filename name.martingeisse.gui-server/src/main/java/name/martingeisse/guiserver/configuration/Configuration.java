/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import name.martingeisse.guiserver.application.ServerConfiguration;
import name.martingeisse.guiserver.application.wicket.GuiWicketApplication;

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
			instance = new Configuration();
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
	private final Map<Class<? extends ConfigurationElement>, Map<String, ConfigurationElement>> elements;
	
	/**
	 * Constructor.
	 * 
	 * @throws IOException on I/O errors
	 * @throws ConfigurationException on errors in a configuration file
	 */
	public Configuration() throws IOException, ConfigurationException {
		File configurationRoot = new File(ServerConfiguration.configurationRoot.getMandatoryValue());
		elements = new ConfigurationBuilder().build(configurationRoot);
	}
	
	/**
	 * Mounts the Wicket URLs needed by this configuration.
	 * 
	 * @param application the Wicket application
	 */
	public void mountWicketUrls(GuiWicketApplication application) {
		for (Map<String, ConfigurationElement> subMap : elements.values()) {
			for (ConfigurationElement element : subMap.values()) {
				element.mountWicketUrls(application);
			}
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
		Map<String, ConfigurationElement> subMap = elements.get(type);
		return (subMap == null ? null : type.cast(subMap.get(path)));
	}

}
