/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.config;

/**
 * Integer-typed configuration property.
 */
public class IntProperty extends AbstractConfigurationProperty<Integer> {

	/**
	 * Constructor.
	 * @param configFile the configuration file
	 * @param name the property name
	 */
	public IntProperty(ConfigurationPropertiesFile configFile, String name) {
		super(configFile, name, null);
	}

	/**
	 * Constructor.
	 * @param configFile the configuration file
	 * @param name the property name
	 * @param staticDefault the default to use if the property is not present in the config file.
	 */
	public IntProperty(ConfigurationPropertiesFile configFile, String name, Integer staticDefault) {
		super(configFile, name, staticDefault);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.config.AbstractConfigurationProperty#convertValue(java.lang.String)
	 */
	@Override
	protected Integer convertValue(String text) throws IllegalArgumentException {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("cannot parse integer property " + getName() + ": " + text);
		}
	}
	
}
