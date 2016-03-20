/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.application;

import java.io.File;
import java.io.IOException;

import name.martingeisse.common.config.BooleanProperty;
import name.martingeisse.common.config.ConfigurationPropertiesFile;
import name.martingeisse.common.config.IntProperty;
import name.martingeisse.common.config.StringProperty;

/**
 * This class provides access to the configuration .properties
 * file through static methods. It must be initialized before use.
 */
public final class Configuration {

	/**
	 * the configurationProperties
	 */
	private static ConfigurationPropertiesFile configurationProperties;
	
	/**
	 * the databaseHostname
	 */
	public static StringProperty databaseHostname;

	/**
	 * the databasePort
	 */
	public static IntProperty databasePort;

	/**
	 * the databaseSchema
	 */
	public static StringProperty databaseSchema;

	/**
	 * the databaseUrl
	 */
	public static StringProperty databaseUrl;
	
	/**
	 * the databaseUsername
	 */
	public static StringProperty databaseUsername;
	
	/**
	 * the databasePassword
	 */
	public static StringProperty databasePassword;
	
	/**
	 * the databaseReplicationUsername
	 */
	public static StringProperty databaseReplicationUsername;
	
	/**
	 * the databaseReplicationPassword
	 */
	public static StringProperty databaseReplicationPassword;
	
	/**
	 * the cdnStartFake
	 */
	public static BooleanProperty cdnStartFake;
	
	/**
	 * the cdnUrl
	 */
	public static StringProperty cdnUrl;
	
	/**
	 * Initializes this class from the specified .properties file.
	 * @param configFilePath the path of the configuration file
	 * @throws IOException on I/O errors
	 */
	public static void initialize(String configFilePath) throws IOException {
		configurationProperties = new ConfigurationPropertiesFile(new File(configFilePath));
		databaseHostname = new StringProperty(configurationProperties, "database.host");
		databasePort = new IntProperty(configurationProperties, "database.port");
		databaseSchema = new StringProperty(configurationProperties, "database.schema");
		databaseUrl = new StringProperty(configurationProperties, "database.url");
		databaseUsername = new StringProperty(configurationProperties, "database.username");
		databasePassword = new StringProperty(configurationProperties, "database.password");
		databaseReplicationUsername = new StringProperty(configurationProperties, "database.replication.username");
		databaseReplicationPassword = new StringProperty(configurationProperties, "database.replication.password");
		cdnStartFake = new BooleanProperty(configurationProperties, "cdn.startFake");
		cdnUrl = new StringProperty(configurationProperties, "cdn.url");
	}

}
