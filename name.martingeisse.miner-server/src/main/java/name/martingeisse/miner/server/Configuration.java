/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides access to miner-server.properties through static getter methods.
 * 
 * This class must be initialized before use.
 */
public class Configuration {

	/**
	 * the databaseMainUrl
	 */
	private static String mainDatabaseUrl;
	
	/**
	 * the mainDatabaseUsername
	 */
	private static String mainDatabaseUsername;
	
	/**
	 * the mainDatabasePassword
	 */
	private static String mainDatabasePassword;
	
	/**
	 * Prevent instantiation.
	 */
	private Configuration() {
	}

	/**
	 * Initializes the configuration from miner-server.properties from the classpath.
	 * @throws IOException on I/O errors
	 */
	public static void initializeFromClasspathConfig() throws IOException {
		Properties properties = new Properties();
		try (InputStream inputStream = Configuration.class.getResourceAsStream("/miner-server.properties")) {
			if (inputStream == null) {
				throw new FileNotFoundException("classpath resource miner-server.properties not found");
			}
			properties.load(inputStream);
		}
		initialize(properties);
	}
	
	/**
	 * Initializes the configuration from the specified configuration file.
	 * @param configFile the configuration file to load
	 * @throws IOException on I/O errors
	 */
	public static void initializeFromConfigFile(File configFile) throws IOException {
		Properties properties = new Properties();
		try (FileInputStream fileInputStream = new FileInputStream(configFile)) {
			properties.load(fileInputStream);
		}
		initialize(properties);
	}

	/**
	 */
	private static void initialize(Properties properties) throws IOException {
		mainDatabaseUrl = loadString(properties, "database.main.url");
		mainDatabaseUsername = loadString(properties, "database.main.username");
		mainDatabasePassword = loadString(properties, "database.main.password");
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static String loadString(Properties properties, String key) {
		String value = properties.getProperty(key);
		if (value == null) {
			throw new RuntimeException("configuration property not found: " + key);
		}
		return value.trim();
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static boolean loadBoolean(Properties properties, String key) {
		String textValue = loadString(properties, key);
		if (textValue.equals("true")) {
			return true;
		} else if (textValue.equals("false")) {
			return false;
		} else {
			throw new RuntimeException("property \"" + key + "\": expected boolean value, found: " + textValue);
		}
	}
	
	/**
	 * Getter method for the mainDatabaseUrl.
	 * @return the mainDatabaseUrl
	 */
	public static String getMainDatabaseUrl() {
		return mainDatabaseUrl;
	}
	
	/**
	 * Getter method for the mainDatabaseUsername.
	 * @return the mainDatabaseUsername
	 */
	public static String getMainDatabaseUsername() {
		return mainDatabaseUsername;
	}
	
	/**
	 * Getter method for the mainDatabasePassword.
	 * @return the mainDatabasePassword
	 */
	public static String getMainDatabasePassword() {
		return mainDatabasePassword;
	}
	
}
