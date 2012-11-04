/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.i18n;

import java.util.Locale;

/**
 * This context never finds any localization properties.
 */
public final class EmptyLocalizationContext implements ILocalizationContext {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.i18n.ILocalizationContext#getLocalizationProperty(java.lang.String, java.util.Locale)
	 */
	@Override
	public String getLocalizationProperty(String key, Locale locale) {
		return null;
	}
	
}
