/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.config;

/**
 * String-typed configuration property.
 *
 * @param <E> the enum type
 */
public class EnumProperty<E extends Enum<E>> extends AbstractConfigurationProperty<E> {

	/**
	 * the enumClass
	 */
	private final Class<E> enumClass;
	
	/**
	 * Constructor.
	 * @param enumClass the class object for the enum type
	 * @param configFile the configuration file
	 * @param name the property name
	 */
	public EnumProperty(Class<E> enumClass, ConfigurationPropertiesFile configFile, String name) {
		this(enumClass, configFile, name, null);
	}

	/**
	 * Constructor.
	 * @param enumClass the class object for the enum type
	 * @param configFile the configuration file
	 * @param name the property name
	 * @param staticDefault the default to use if the property is not present in the config file.
	 */
	public EnumProperty(Class<E> enumClass, ConfigurationPropertiesFile configFile, String name, E staticDefault) {
		super(configFile, name, staticDefault);
		this.enumClass = enumClass;
		if (enumClass == null) {
			throw new IllegalArgumentException("enumClass argument is null");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.config.AbstractConfigurationProperty#convertValue(java.lang.String)
	 */
	@Override
	protected E convertValue(String text) throws IllegalArgumentException {
		try {
			return Enum.valueOf(enumClass, text);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("invalid value of type " + enumClass.getSimpleName() + ": " + text);
		}
	}
	
}
