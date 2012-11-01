/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.authentication;

import java.util.HashMap;

/**
 * Default implementation for {@link IUserProperties} that simply
 * stores properties in a {@link HashMap}.
 */
public class UserProperties extends HashMap<String, Object> implements IUserProperties {

	/**
	 * Constructor.
	 */
	public UserProperties() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.security.authentication.IUserProperties#containsKey(java.lang.String)
	 */
	@Override
	public boolean containsKey(String key) {
		return containsKey((Object)key);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.security.authentication.IUserProperties#getKeys()
	 */
	@Override
	public Iterable<String> getKeys() {
		return keySet();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.security.authentication.IUserProperties#get(java.lang.String)
	 */
	@Override
	public Object get(String key) {
		return get((Object)key);
	}
		
}
