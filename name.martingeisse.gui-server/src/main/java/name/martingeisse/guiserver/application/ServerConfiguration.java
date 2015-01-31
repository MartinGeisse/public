/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.guiserver.application;

import java.io.File;
import java.io.IOException;

import name.martingeisse.common.config.ConfigurationPropertiesFile;
import name.martingeisse.common.config.StringProperty;

/**
 * This class provides access to the configuration .properties
 * file through static methods. It must be initialized before use.
 */
public final class ServerConfiguration {

	/**
	 * the configurationProperties
	 */
	private static ConfigurationPropertiesFile configurationProperties;
	
	/**
	 * the guiConfigurationFile
	 */
	public static StringProperty guiConfigurationFile;

	/**
	 * Initializes this class from the specified .properties file.
	 * @param configFilePath the path of the configuration file
	 * @throws IOException on I/O errors
	 */
	public static void initialize(String configFilePath) throws IOException {
		configurationProperties = new ConfigurationPropertiesFile(new File(configFilePath));
		guiConfigurationFile = new StringProperty(configurationProperties, "guiConfigurationFile");
	}

}
