/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.i18n;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

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
		this.locale = ParameterUtil.ensureNotNull(locale, "locale");;
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
		this.locale = ParameterUtil.ensureNotNull(locale, "locale");;
	}

	/**
	 * Obtains the localization property for the specified key.
	 * @param key the key
	 * @return the localization property
	 */
	public String getLocalizationProperty(String key) {
		ParameterUtil.ensureNotNull(key, "key");
		Iterator<ILocalizationContext> iterator = descendingIterator();
		while (iterator.hasNext()) {
			ILocalizationContext context = iterator.next();
			String property = context.getLocalizationProperty(key, locale);
			if (property != null) {
				return property;
			}
		}
		return key;
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
		String property = additionalContext.getLocalizationProperty(key, locale);
		return (property != null ? property : getLocalizationProperty(key));
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
