/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Keeps the data from a .properties file for configuration.
 */
public final class ConfigurationPropertiesFile {

	/**
	 * the properties
	 */
	private final Properties properties;

	/**
	 * Constructor.
	 * @param file the configuration file
	 * @throws IOException on I/O errors
	 */
	public ConfigurationPropertiesFile(File file) throws IOException {
		properties = new Properties();
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			properties.load(fileInputStream);
		}
	}

	/**
	 * 
	 */
	String getProperty(String name) {
		return properties.getProperty(name);
	}
	
}
