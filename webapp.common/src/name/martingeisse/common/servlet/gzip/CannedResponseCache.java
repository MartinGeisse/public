/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.servlet.gzip;

import java.util.HashMap;
import java.util.Map;

/**
 * A cache for canned responses.
 */
public class CannedResponseCache {

	/**
	 * the keyToValue
	 */
	private Map<CannedResponseCacheKey, CannedResponse> keyToValue;
	
	/**
	 * Constructor.
	 */
	public CannedResponseCache() {
		this.keyToValue = new HashMap<CannedResponseCacheKey, CannedResponse>();
	}
	
	/**
	 * Puts a value into the cache.
	 * @param key the cache key
	 * @param value the cache value
	 */
	public synchronized void put(CannedResponseCacheKey key, CannedResponse value) {
		keyToValue.put(key, value);
	}
	
	/**
	 * Gets a value from the cache.
	 * @param key the cache key
	 * @return the cache value
	 */
	public synchronized CannedResponse get(CannedResponseCacheKey key) {
		return keyToValue.get(key);
	}
	
}
