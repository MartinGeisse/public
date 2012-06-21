/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.keycount;

/**
 * This class represents a key/count pair, typically containing
 * a search key and the corresponding hit count.
 */
public final class KeyCountEntry {

	/**
	 * the key
	 */
	private final String key;
	
	/**
	 * the count
	 */
	private final long count;
	
	/**
	 * Constructor.
	 * @param key the key
	 * @param count the count
	 */
	public KeyCountEntry(String key, long count) {
		this.key = key;
		this.count = count;
	}

	/**
	 * Getter method for the key.
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Getter method for the count.
	 * @return the count
	 */
	public long getCount() {
		return count;
	}
	
}
