/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import java.io.File;
import java.io.IOException;

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
	 * Constructor.
	 * 
	 * @throws IOException on I/O errors
	 * @throws ConfigurationException on errors in a configuration file
	 */
	public Configuration() throws IOException, ConfigurationException {
		this.rootNamespace = ConfigurationParser.parse(new File("resource/demo/gui.json"));
	}

	/**
	 * Getter method for the rootNamespace.
	 * @return the rootNamespace
	 */
	public ConfigurationNamespace getRootNamespace() {
		return rootNamespace;
	}
	
}
