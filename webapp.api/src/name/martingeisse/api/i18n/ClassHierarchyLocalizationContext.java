/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import name.martingeisse.common.util.ParameterUtil;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

/**
 * The is the most common localization context. It loops through the classes,
 * starting with a specific class and stopping with {@link Object}. For each
 * class in the hierarchy, it looks for a property file named
 * "classname_locale.properties". For example, for the class {@link String}
 * and locale en_US it would look at
 * 
 * 		java/lang/String_en_US.properties
 * 		java/lang/Object_en_US.properties
 *
 */
public final class ClassHierarchyLocalizationContext implements ILocalizationContext {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ClassHierarchyLocalizationContext.class);
	
	/**
	 * the cachedProperties
	 */
	private static final ConcurrentHashMap<Class<?>, ILocalizationContext> cachedInstances = new ConcurrentHashMap<Class<?>, ILocalizationContext>();
	
	// static initializer
	static {
		
		// this saves ourselves a HashMap read for every localized property
		cachedInstances.put(Object.class, new EmptyLocalizationContext());

	}
	
	/**
	 * Returns the instance of this class for the specified origin class.
	 * 
	 * Note that synchronization in this method allows two independent callers
	 * to create an instance of this class. We avoid using both by calling
	 * get() on the map in the final step, so the only effect is that one
	 * instance is created in vain (all instances have equal content).
	 * 
	 * @param c the class
	 * @return the instance of this class
	 */
	public static ILocalizationContext getForClass(Class<?> c) {
		if (c == null) {
			return null;
		}
		ILocalizationContext instance = cachedInstances.get(c);
		if (instance != null) {
			return instance;
		}
		instance = new ClassHierarchyLocalizationContext(c);
		cachedInstances.putIfAbsent(c, instance);
		return cachedInstances.get(c);
	}
	
	/**
	 * the origin
	 */
	private final Class<?> origin;
	
	/**
	 * the parentClassContext
	 */
	private final ILocalizationContext parentClassContext;
	
	/**
	 * the localizationProperties
	 */
	private final HashMap<Locale, Map<String, String>> localizationProperties; 
	
	/**
	 * Constructor.
	 * @param origin the class to start with
	 */
	public ClassHierarchyLocalizationContext(Class<?> origin) {
		this.origin = ParameterUtil.ensureNotNull(origin, "origin");
		this.parentClassContext = getForClass(origin.getSuperclass());
		this.localizationProperties = new HashMap<Locale, Map<String, String>>();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.i18n.ILocalizationContext#getLocalizationProperty(java.lang.String, java.util.Locale)
	 */
	@Override
	public String getLocalizationProperty(String key, Locale locale) {
		String property = getOriginLocalizationProperty(key, locale);
		if (property != null) {
			return property;
		} else if (parentClassContext != null) {
			return parentClassContext.getLocalizationProperty(key, locale);
		} else {
			return null;
		}
	}

	/**
	 * Returns the localization property only for the origin class,
	 * not taking base classes into account.
	 */
	private String getOriginLocalizationProperty(String key, Locale locale) {
		Map<String, String> properties;

		// look for cached properties
		properties = localizationProperties.get(locale);
		if (properties != null) {
			return properties.get(key);
		}
		
		// try to load a .properties file, using EMPTY_MAP as a fallback if none exists
		properties = loadProperties(origin, locale);
		localizationProperties.put(locale, properties);
		return properties.get(key);
		
	}

	/**
	 * Loads the .properties file for the origin class and the specified locale and returns
	 * either its contents as a map, or an empty map if the file cannot be found.
	 * 
	 * This method is static to emphasize that it does not use or affect the calling instance
	 * in any other way.
	 * 
	 * @param origin the origin class
	 * @param locale the locale
	 * @return the properties
	 */
	private static Map<String, String> loadProperties(Class<?> origin, Locale locale) {
		String filename = origin.getSimpleName() + '_' + locale + ".properties";
		try {
			
			// try to open the file, and fall back to an empty map if that fails
			InputStream inputStream = origin.getResourceAsStream(filename);
			if (inputStream == null) {
				@SuppressWarnings("unchecked")
				Map<String, String> emptyMap = MapUtils.EMPTY_MAP;
				return emptyMap;
			}
			
			// load the properties
			Properties properties = new Properties();
			properties.load(inputStream);
			inputStream.close();
			@SuppressWarnings("unchecked")
			Map<String, String> typedProperties = (Map<String, String>)(Map<?, ?>)properties;
			return typedProperties;
			
		} catch (IOException e) {
			logger.error("could not load i18n property file", e);
			return null;
		}		
	}
	
}
