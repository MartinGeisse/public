/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Contains global localization settings.
 * 
 * This class defines three levels of localization fallbacks:
 * 
 * - property level: When a property cannot be found in any .properties file for any locale,
 *   then the property-level fallback may specify another locale whose .properties file
 *   might contain the property. Multiple locales can be chained in this way. This fallback
 *   level can be used when the localization texts of one locale are mostly similar to those
 *   of another locale, with only slight deviations (like en-GB is similar to en-US).
 * 
 * - language level: When the search for a localized text through all levels of the context
 *   stack has failed, then the language-level fallback may specify another locale with
 *   which the search shall be restarted. Multiple locales can be chained in this way.
 *   This fallback level can be used when two locales use entirely different languages,
 *   but apply to the same or nearby countries, such that users will accept the fallback
 *   language
 *   
 * - global fallback: When everything else has failed, this locale is used for another
 *   full search for the property. This is like an implicit end node for all language-level
 *   chains.
 *   
 * The main difference between property-level and language-level fallback is its precedence
 * compared to the regular property search order in base classes and in the context stack.
 * Property-level fallbacks take precedence over base classes and parent contexts, so they
 * work very much like replacing individual properties in one locale to obtain another
 * locale. Language-level fallbacks, on the other hand, only take place when a property
 * cannot be found even in base classes and parent contexts, and so can be used to obtain
 * localized texts which the user can understand, in cases when texts for the requested
 * locale are missing (such as when translation is still in progress). Unlike
 * property-level fallback, language-level fallback is typically not desirable, but still
 * better than just using the global fallback or even missing texts.
 */
public final class LocalizationConfiguration {

	/**
	 * the propertyLevelFallbackMap
	 */
	private final Map<Locale, Locale> propertyLevelFallbackMap = new HashMap<Locale, Locale>();
	
	/**
	 * the languageLevelFallbackMap
	 */
	private final Map<Locale, Locale> languageLevelFallbackMap = new HashMap<Locale, Locale>();
	
	/**
	 * the globalFallback
	 */
	private Locale globalFallback;
	
	/**
	 * Constructor.
	 */
	public LocalizationConfiguration() {
	}
	
	/**
	 * Getter method for the propertyLevelFallbackMap.
	 * @return the propertyLevelFallbackMap
	 */
	public Map<Locale, Locale> getPropertyLevelFallbackMap() {
		return propertyLevelFallbackMap;
	}
	
	/**
	 * Getter method for the languageLevelFallbackMap.
	 * @return the languageLevelFallbackMap
	 */
	public Map<Locale, Locale> getLanguageLevelFallbackMap() {
		return languageLevelFallbackMap;
	}
	
	/**
	 * Getter method for the globalFallback.
	 * @return the globalFallback
	 */
	public Locale getGlobalFallback() {
		return globalFallback;
	}
	
	/**
	 * Setter method for the globalFallback.
	 * @param globalFallback the globalFallback to set
	 */
	public void setGlobalFallback(Locale globalFallback) {
		this.globalFallback = globalFallback;
	}

}
