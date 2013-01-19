/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides access to webide.properties through static getter methods.
 * 
 * This class must be initialized before use.
 */
public class Configuration {

	/**
	 * the deployedFolderLayout
	 */
	private static boolean deployedFolderLayout;
	
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
	 * Initializes the configuration from webide.properties.
	 * @throws IOException on I/O errors
	 */
	public static void initialize() throws IOException {
		Properties properties = new Properties();
		InputStream fileInputStream = Configuration.class.getResourceAsStream("/webide.properties");
		if (fileInputStream == null) {
			throw new FileNotFoundException("classpath resource webide.properties not found");
		}
		properties.load(fileInputStream);
		fileInputStream.close();
		deployedFolderLayout = loadBoolean(properties, "path.deployed");
		mainDatabaseUrl = loadString(properties, "database.main.url");
		mainDatabaseUsername = loadString(properties, "database.main.username");
		mainDatabasePassword = loadString(properties, "database.main.password");
	}
	
	/**
	 * 
	 */
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
	 * Getter method for the deployedFolderLayout.
	 * @return the deployedFolderLayout
	 */
	public static boolean isDeployedFolderLayout() {
		return deployedFolderLayout;
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
