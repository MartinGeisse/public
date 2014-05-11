/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.config;

/**
 * String-typed configuration property.
 */
public class StringProperty extends AbstractConfigurationProperty<String> {

	/**
	 * Constructor.
	 * @param configFile the configuration file
	 * @param name the property name
	 */
	public StringProperty(ConfigurationPropertiesFile configFile, String name) {
		super(configFile, name, null);
	}

	/**
	 * Constructor.
	 * @param configFile the configuration file
	 * @param name the property name
	 * @param staticDefault the default to use if the property is not present in the config file.
	 */
	public StringProperty(ConfigurationPropertiesFile configFile, String name, String staticDefault) {
		super(configFile, name, staticDefault);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.config.AbstractConfigurationProperty#convertValue(java.lang.String)
	 */
	@Override
	protected String convertValue(String text) throws IllegalArgumentException {
		return text;
	}
	
}
