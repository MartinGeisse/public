/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.i18n;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import name.martingeisse.api.servlet.RestfulServlet;
import name.martingeisse.common.util.ParameterUtil;

/**
 * Stores a stack of localization contexts as well as the current thread's
 * locale.
 * 
 * This class will never return null as a localization property. If no
 * property can be found, it returns the key.
 */
final class LocalizationContextStack extends LinkedList<ILocalizationContext> {

	/**
	 * the locale
	 */
	private Locale locale;

	/**
	 * Constructor.
	 * @param locale the locale
	 */
	public LocalizationContextStack(final Locale locale) {
		this.locale = ParameterUtil.ensureNotNull(locale, "locale");
	}

	/**
	 * Getter method for the locale.
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Setter method for the locale.
	 * @param locale the locale to set
	 */
	public void setLocale(final Locale locale) {
		this.locale = ParameterUtil.ensureNotNull(locale, "locale");
	}

	/**
	 * Obtains the localization property for the specified key.
	 * @param key the key
	 * @return the localization property
	 */
	public String getLocalizationProperty(String key) {
		ParameterUtil.ensureNotNull(key, "key");
		return getLocalizationProperty((ILocalizationContext)null, key);
	}
	
	/**
	 * Obtains the localization property for the specified key
	 * and the current thread's context stack, including the
	 * additional context.
	 * @param additionalContext a context to treat as an implicit top-of-stack
	 * @param key the key
	 * @return the localization property
	 */
	public String getLocalizationProperty(ILocalizationContext additionalContext, String key) {
		ParameterUtil.ensureNotNull(key, "key");
		LocalizationConfiguration configuration = RestfulServlet.getConfiguration().getLocalizationConfiguration();
		Locale currentLocale = locale;

		while (currentLocale != null) {
			String property = getLocalizationPropertyForLocale(additionalContext, key, currentLocale);
			if (property != null) {
				return property;
			}
			currentLocale = configuration.getLanguageLevelFallbackMap().get(currentLocale);
		}
		
		Locale globalFallbackLocale = configuration.getGlobalFallback();
		if (globalFallbackLocale != null) {
			String property = getLocalizationPropertyForLocale(additionalContext, key, globalFallbackLocale);
			if (property != null) {
				return property;
			}
		}
		
		return key;
	}
	
	/**
	 * Helper method that ignores language-level fallback locales.
	 */
	private String getLocalizationPropertyForLocale(ILocalizationContext additionalContext, String key, Locale currentLocale) {
		
		// try the additional context, if any
		if (additionalContext != null) {
			String additionalContextProperty = additionalContext.getLocalizationProperty(key, currentLocale);
			if (additionalContextProperty != null) {
				return additionalContextProperty;
			}
		}
		
		// try the context stack
		Iterator<ILocalizationContext> iterator = descendingIterator();
		while (iterator.hasNext()) {
			ILocalizationContext context = iterator.next();
			String property = context.getLocalizationProperty(key, currentLocale);
			if (property != null) {
				return property;
			}
		}
	
		return null;
	}
	
	/**
	 * Obtains the localization property for the specified key
	 * and the current thread's context stack, including the
	 * additional context.
	 * @param additionalContext a context to treat as an implicit top-of-stack
	 * @param key the key
	 * @return the localization property
	 */
	public String getLocalizationProperty(Class<?> additionalContext, String key) {
		ParameterUtil.ensureNotNull(key, "key");
		return getLocalizationProperty(ClassHierarchyLocalizationContext.getForClass(additionalContext), key);
	}
	
}
