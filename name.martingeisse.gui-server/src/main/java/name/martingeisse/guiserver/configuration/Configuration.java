/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

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
	 * the rootNamespace
	 */
	private final ConfigurationNamespace rootNamespace;
	
	/**
	 * the rootUrlPageConfiguration
	 */
	private final PageConfiguration rootUrlPageConfiguration;
	
	/**
	 * Constructor.
	 * 
	 * @throws IOException on I/O errors
	 * @throws ConfigurationException on errors in a configuration file
	 */
	public Configuration() throws IOException, ConfigurationException {
		rootNamespace = ConfigurationParser.parse(new File("resource/demo/gui.json"));
		rootNamespace.initializeRoot();
		rootUrlPageConfiguration = new SingleConfigurationElementSearch<PageConfiguration>(PageConfiguration.class) {
			@Override
			public boolean checkMatch(PageConfiguration element) {
				return element.getUrlPath().equals("");
			}
		}.execute();
		if (rootUrlPageConfiguration == null) {
			throw new ConfigurationException("no page defined for the root URL");
		}
	}

	/**
	 * Getter method for the rootNamespace.
	 * @return the rootNamespace
	 */
	public ConfigurationNamespace getRootNamespace() {
		return rootNamespace;
	}
	
	/**
	 * Getter method for the rootUrlPageConfiguration.
	 * @return the rootUrlPageConfiguration
	 */
	public PageConfiguration getRootUrlPageConfiguration() {
		return rootUrlPageConfiguration;
	}

	/**
	 * Returns a typed configuration element using its absolute path. This method is
	 * like calling {@link #getElementAbsolute(String)} and then type-casting the
	 * result, but gives better error messages in case of a type error.
	 * 
	 * @param path the path to the configuration element (must be absolute)
	 * @return the configuration element
	 */
	public <T extends ConfigurationElement> T getElementAbsolute(String path, Class<T> theClass) {
		ConfigurationElement result = getElementAbsolute(path);
		try {
			return theClass.cast(result);
		} catch (ClassCastException e) {
			throw new ClassCastException("configuration element at path '" + path + "' is not a " + theClass.getSimpleName());
		}
	}

	/**
	 * Returns a configuration element using its absolute path.
	 * @param path the path to the configuration element (must be absolute)
	 * @return the configuration element
	 */
	public ConfigurationElement getElementAbsolute(String path) {
		if (path.equals("/")) {
			return rootNamespace;
		}
		if (path.contains("//") || path.endsWith("/")) {
			throw new IllegalArgumentException("invalid path: " + path);
		}
		if (path.isEmpty() || path.charAt(0) != '/') {
			throw new IllegalArgumentException("not an absolute path: " + path);
		}
		String[] segments = StringUtils.split(path.substring(1), '/');
		ConfigurationElement currentElement = rootNamespace;
		for (String segment : segments) {
			if (!(currentElement instanceof ConfigurationNamespace)) {
				throw new RuntimeException("segment '" + segment + "' in path '" + path + "' is not a namespace");
			}
			currentElement = ((ConfigurationNamespace)currentElement).getElements().get(segment);
			if (currentElement == null) {
				throw new RuntimeException("segment '" + segment + "' in path '" + path + "' does not exist");
			}
		}
		return currentElement;
	}
	
}
