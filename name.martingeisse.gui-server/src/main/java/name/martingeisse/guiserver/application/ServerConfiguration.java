/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.application;

import java.io.File;
import java.io.IOException;

import name.martingeisse.common.config.ConfigurationPropertiesFile;

/**
 * This class provides access to the configuration .properties
 * file through static methods. It must be initialized before use.
 */
public final class ServerConfiguration {

	/**
	 * the configurationProperties
	 */
	@SuppressWarnings("unused")
	private static ConfigurationPropertiesFile configurationProperties;

	/**
	 * Initializes this class from the specified .properties file.
	 * @param configFilePath the path of the configuration file
	 * @throws IOException on I/O errors
	 */
	public static void initialize(String configFilePath) throws IOException {
		configurationProperties = new ConfigurationPropertiesFile(new File(configFilePath));
	}

}
