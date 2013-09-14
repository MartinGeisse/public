/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.config;

/**
 * Class-object-typed configuration property, optionally limited to subtypes
 * of a specific class or interface. Pass {@link Object} for the base
 * type to allow any type.
 * 
 * @param <B> the base type
 */
public class ClassProperty<B> extends AbstractConfigurationProperty<Class<? extends B>> {

	/**
	 * the baseType
	 */
	private final Class<B> baseType;
	
	/**
	 * Constructor.
	 * @param baseType the base type which the specified class must extend
	 * @param configFile the configuration file
	 * @param name the property name
	 */
	public ClassProperty(Class<B> baseType, ConfigurationPropertiesFile configFile, String name) {
		this(baseType, configFile, name, null);
	}

	/**
	 * Constructor.
	 * @param baseType the base type which the specified class must extend
	 * @param configFile the configuration file
	 * @param name the property name
	 * @param staticDefault the default to use if the property is not present in the config file.
	 */
	public ClassProperty(Class<B> baseType, ConfigurationPropertiesFile configFile, String name, Class<? extends B> staticDefault) {
		super(configFile, name, staticDefault);
		this.baseType = baseType;
		if (baseType == null) {
			throw new IllegalArgumentException("baseType argument is null");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.config.AbstractConfigurationProperty#convertValue(java.lang.String)
	 */
	@Override
	protected Class<? extends B> convertValue(String text) throws IllegalArgumentException {
		
		// load the class
		Class<?> c;
		try {
			c = getClass().getClassLoader().loadClass(text);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("class not found for property " + getName() + ": " + text);
		}
		
		// make sure it extends the base type
		try {
			return c.asSubclass(baseType);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("type error for property " + getName() + ": class does not extend " + baseType.getName());
		}
		
	}
	
	/**
	 * Creates an instance of the specified class using its no-arg constructor.
	 * @return the instance
	 */
	public B createInstance() {
		try {
			return getMandatoryValue().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
