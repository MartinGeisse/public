/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.config;

/**
 * String-typed configuration property.
 */
public class BooleanProperty extends AbstractConfigurationProperty<Boolean> {

	/**
	 * Constructor.
	 * @param configFile the configuration file
	 * @param name the property name
	 */
	public BooleanProperty(ConfigurationPropertiesFile configFile, String name) {
		super(configFile, name, null);
	}

	/**
	 * Constructor.
	 * @param configFile the configuration file
	 * @param name the property name
	 * @param staticDefault the default to use if the property is not present in the config file.
	 */
	public BooleanProperty(ConfigurationPropertiesFile configFile, String name, Boolean staticDefault) {
		super(configFile, name, staticDefault);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.config.AbstractConfigurationProperty#convertValue(java.lang.String)
	 */
	@Override
	protected Boolean convertValue(String text) throws IllegalArgumentException {
		if (text.equals("true")) {
			return true;
		} else if (text.equals("false")) {
			return false;
		} else {
			throw new RuntimeException("invalid configuration value for property " + getName() + ": " + text);
		}
	}
	
}
