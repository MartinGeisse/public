/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.i18n;

import java.util.Locale;

/**
 * Implementations are able to return a localization property, given a
 * key and locale.
 */
public interface ILocalizationContext {

	/**
	 * Returns the localization property for the specified key, or null
	 * if no property was found.
	 * 
	 * @param key the key
	 * @param locale the locale
	 * @return the localization property or null
	 */
	public String getLocalizationProperty(String key, Locale locale);

}
