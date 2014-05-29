/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.security.authentication;


/**
 * This interface is implemented by data objects that know some
 * or all of a user's properties. Properties are simple key/value
 * pairs with string keys and arbitrary values.
 */
public interface IUserProperties {

	/**
	 * Checks whether this property set is empty.
	 * @return true if empty, false if nonempty
	 */
	public boolean isEmpty();

	/**
	 * @return the number of key/value pairs in this object
	 */
	public int size();

	/**
	 * Checks if the specified key is contained in this property set.
	 * @param key the key
	 * @return true if this object contains the key, false if not
	 */
	public boolean containsKey(String key);

	/**
	 * Returns an {@link Iterable} object for the keys contained in
	 * this object.
	 * @return the keys
	 */
	public Iterable<String> getKeys();

	/**
	 * Returns the value for the specified key.
	 * @param key the key
	 * @return the value
	 */
	public Object get(String key);

}
