/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

/**
 * This is a singleton that is placed in caches to represent null values.
 * Such a null object is needed since cache implementations treat null
 * differently or simply disallow null values.
 * 
 * Implemented as an enum to support singleton-ness in serialization.
 * 
 * Note that application normally does not have to deal with this type.
 * Specifically, cache implementations should return null to represent
 * cached null. This type is only needed when a cache is accessed
 * manually.
 */
public enum CachedNull {

	/**
	 * the singleton instance
	 */
	INSTANCE;
	
}
