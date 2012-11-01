/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.i18n;

import java.util.Locale;

/**
 * This interface represents the ability to return a localized text
 * for a key and a locale.
 */
public interface ILocalizer {

	/**
	 * Returns the localized text for the specified key and locale.
	 * @param key the key
	 * @param locale the locale
	 * @return the localized text
	 */
	public String get(String key, Locale locale);
	
}
