/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.IStringResourceLoader;

/**
 * This implementation of {@link IStringResourceLoader} uses a fixed
 * prefix. If any key string beings with that prefix, this loader
 * returns the remainder of the string as the value for that key.
 * 
 * This class is typically used to turn missing text properties
 * into "soft" errors.
 */
public class PrefixedIdentityStringResourceLoader implements IStringResourceLoader {

	/**
	 * the prefix
	 */
	private String prefix;

	/**
	 * Constructor.
	 */
	public PrefixedIdentityStringResourceLoader() {
	}

	/**
	 * Constructor.
	 * @param prefix the prefix
	 */
	public PrefixedIdentityStringResourceLoader(final String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Getter method for the prefix.
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Setter method for the prefix.
	 * @param prefix the prefix to set
	 */
	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.resource.loader.IStringResourceLoader#loadStringResource(java.lang.Class, java.lang.String, java.util.Locale, java.lang.String, java.lang.String)
	 */
	@Override
	public String loadStringResource(final Class<?> clazz, final String key, final Locale locale, final String style, final String variation) {
		return getString(key);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.resource.loader.IStringResourceLoader#loadStringResource(org.apache.wicket.Component, java.lang.String, java.util.Locale, java.lang.String, java.lang.String)
	 */
	@Override
	public String loadStringResource(final Component component, final String key, final Locale locale, final String style, final String variation) {
		return getString(key);
	}

	/**
	 * @param key
	 * @return
	 */
	private String getString(final String key) {
		if (key.startsWith(prefix)) {
			return key.substring(prefix.length());
		} else {
			return null;
		}
	}

}
