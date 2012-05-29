/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class wraps a hash map in which the keys are {@link Class} objects
 * of which the corresponding values are instances. For example,
 * a {@link ClassKeyedContainer} can use the class object for {@link String}
 * as a key, but the corresponding value must be a string. This can be used
 * as an extensible general-purpose container that contains data objects
 * from various modules of an application and returns them in a type-safe
 * way and without the potential for name collisions.
 * 
 * The rule is that each value must be an instance of the key, not necessarily
 * that the value's getClass() method returns the key. This allows the key
 * to be a superclass or interface of which the value is an instance. This is
 * useful for modules that store data objects in the container whose outside
 * behavior is defined only by an interface.
 * 
 * The intention is that keys are very specific classes from application modules.
 * The above example of using the Class object of class {@link String}, or even
 * {@link Object}, is possible, but does not make very much sense in the way
 * this container is intended to be used.
 */
public final class ClassKeyedContainer implements Serializable {

	/**
	 * the map
	 */
	private HashMap<Class<?>, Object> map;
	
	/**
	 * Constructor.
	 */
	public ClassKeyedContainer() {
		this.map = new HashMap<Class<?>, Object>();
	}
	
	/**
	 * Adds an object using its implementation class as the key.
	 * @param value the object to add
	 * @return the previously stored object for the key
	 */
	public Object add(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		return map.put(value.getClass(), value);
	}
	
	/**
	 * Adds a value using the specified key.
	 * @param <T> the static key type
	 * @param key the key Class object
	 * @param value the value to add
	 * @return the previously stored value for that key
	 */
	public <T> T add(Class<T> key, T value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		return key.cast(map.put(key, value));
	}
	
	/**
	 * Returns the value for the specified key, or null of no such value
	 * is stored in this container.
	 * @param <T> the static key type
	 * @param key the key Class object
	 * @return the value stored for that key
	 */
	public <T> T get(Class<T> key) {
		return key.cast(map.get(key));
	}

	/**
	 * Removes and returns the value for the specified key, if any.
	 * Returns null if no such value was stored.
	 * @param <T> the static key type
	 * @param key the key Class object
	 * @return the value that was stored for that key
	 */
	public <T> T remove(Class<T> key) {
		return key.cast(map.remove(key));
	}

}
