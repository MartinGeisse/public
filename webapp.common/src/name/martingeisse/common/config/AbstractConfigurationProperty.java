/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.config;

import name.martingeisse.common.util.ParameterUtil;

/**
 * Base class for properties that are loaded from a {@link ConfigurationPropertiesFile}.
 * 
 * @param <T> the property type
 */
public abstract class AbstractConfigurationProperty<T> {

	/**
	 * the configFile
	 */
	private final ConfigurationPropertiesFile configFile;
	
	/**
	 * the name
	 */
	private final String name;
	
	/**
	 * the valueInitialized
	 */
	private boolean valueInitialized;
	
	/**
	 * the value
	 */
	private T value;
	
	/**
	 * Constructor.
	 * 
	 * @param configFile the configuration file
	 * @param name the property name
	 * @param staticDefault the default to use if the property is not present in the config file.
	 * This default will affect {@link #getMandatoryValue()} and {@link #getOptionalValue()} too,
	 * tricking them into thinking that the value was set in the properties file.
	 */
	public AbstractConfigurationProperty(ConfigurationPropertiesFile configFile, String name, T staticDefault) {
		ParameterUtil.ensureNotNull(configFile, "configFile");
		ParameterUtil.ensureNotNull(name, "name");
		this.configFile = configFile;
		this.name = name;
		this.valueInitialized = false;
		this.value = staticDefault;
	}

	/**
	 * @return the textual value of the property as listed in the config file
	 */
	protected final String getTextValue() {
		return configFile.getProperty(name);
	}
	
	/**
	 * Validates the value text, then converts the value text to the internal value.
	 * @param text the value text
	 * @return the value
	 * @throws IllegalArgumentException if the value is invalid
	 */
	protected abstract T convertValue(String text) throws IllegalArgumentException;

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * 
	 */
	private void lazyInitializeValue() {
		if (!valueInitialized) {
			String textValue = getTextValue();
			T convertedValue = (textValue == null ? null : convertValue(textValue));
			if (convertedValue != null) {
				this.value = convertedValue;
			}
			valueInitialized = true;
		}
	}
	
	/**
	 * Getter method for the value. Throws an {@link IllegalStateException} if
	 * the property is not present in the configuration file.
	 * 
	 * @return the value
	 */
	public final T getMandatoryValue() {
		lazyInitializeValue();
		if (value == null) {
			throw new IllegalStateException("configuration property not set: " + name);
		}
		return value;
	}
	
	/**
	 * Getter method for the value. Returns null if
	 * the property is not present in the configuration file.
	 * 
	 * @return the value
	 */
	public final T getOptionalValue() {
		lazyInitializeValue();
		return value;
	}

	/**
	 * Getter method for the value. Returns the specified default value if
	 * the property is not present in the configuration file.
	 * 
	 * @param dynamicDefault the default
	 * @return the value
	 */
	public final T getValueOrDefault(T dynamicDefault) {
		lazyInitializeValue();
		return (value == null ? dynamicDefault : value);
	}
	
}
