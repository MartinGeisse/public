/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import name.martingeisse.common.util.string.EmptyIterator;

/**
 * This class wraps a hash map in which the keys are {@link Class} objects
 * of which the corresponding values are lists of instances. For example,
 * a {@link ClassKeyedListContainer} can use the class object for {@link String}
 * as a key, but the corresponding value list must contain only strings. This
 * can be used as an extensible general-purpose container that contains data
 * objects from various modules of an application and returns them in a type-safe
 * way and without the potential for name collisions.
 * 
 * The rule is that each value must be an instance of the key, not necessarily
 * that the value's getClass() method returns the key. This allows the key
 * to be a superclass or interface of which the value is an instance. This is
 * useful for modules that store data objects in the container whose outside
 * behavior is defined only by an interface.
 * 
 * The intention is that keys are module-specific classes or interfaces from
 * application modules. The above example of using the Class object of class
 * {@link String}, or even {@link Object}, is possible, but does not make very
 * much sense in the way this container is intended to be used.
 * 
 * The types of contained objects can be restricted by base type B.
 * 
 * This class is written in a way that does not define different meanings
 * to keys without value lists vs. keys with empty value lists. This class
 * does distinguish these cases, but solely to avoid creating lots of
 * empty list instances.
 * 
 * @param <B> the base type of all contained objects
 */
public final class ClassKeyedListContainer<B> implements Serializable {

	/**
	 * the map
	 */
	private HashMap<Class<? extends B>, List<B>> map;
	
	/**
	 * Constructor.
	 */
	public ClassKeyedListContainer() {
		this.map = new HashMap<Class<? extends B>, List<B>>();
	}
	
	/**
	 * Needed to make value lists appear as using the corresponding
	 * subtype T of B, not B itself.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object value) {
		return (T)value;
	}
	
	/**
	 * Returns the value list for the specified key. May return null
	 * instead of the empty list if no list has been created for the key yet.
	 * @param <T> the static key type
	 * @param key the key Class object
	 * @return the value stored for that key
	 */
	public <T extends B> List<T> getCachedList(Class<T> key) {
		return unsafeCast(map.get(key));
	}
	
	/**
	 * Returns the list for the specified key, creating it if necessary.
	 * @param <T> the static key type
	 * @param key the key
	 * @return the list
	 */
	public <T extends B> List<T> getOrCreateList(Class<T> key) {
		List<B> list = map.get(key);
		if (list == null) {
			list = new ArrayList<B>();
			map.put(key, list);
		}
		return unsafeCast(list);
	}
	
	/**
	 * Adds a value using the specified key.
	 * @param <T> the static key type
	 * @param key the key Class object
	 * @param value the value to add
	 */
	public <T extends B> void add(Class<T> key, T value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		getOrCreateList(key).add(value);
	}

	/**
	 * Returns an {@link Iterable} for all values associated with the specified
	 * key. The returned iterable is backed by this container and will return the
	 * value objects contained by this container at the time an iterator is created
	 * from it. The iterator itself may or may not be backed by this container, so
	 * the effect of changes on a previously created iterator are unspecified.
	 * 
	 * @param <T> the static key type
	 * @param key the key Class object
	 * @return an {@link Iterable} for the values for the specified key,
	 * backed by this container
	 */
	public <T extends B> Iterable<T> getIterable(Class<T> key) {
		return new MyIterable<T>(key);
	}
	
	/**
	 * Implementation of {@link Iterable} that looks up the value list
	 * at the time an {@link Iterator} is created from it.
	 *
	 * @param <T> the value type
	 */
	private class MyIterable<T extends B> implements Iterable<T> {
		
		/**
		 * the key
		 */
		private Class<T> key;

		/**
		 * Constructor.
		 * @param key the key to iterate values for
		 */
		public MyIterable(Class<T> key) {
			this.key = key;
		}

		/* (non-Javadoc)
		 * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<T> iterator() {
			List<T> cachedList = getCachedList(key);
			return (cachedList == null ? new EmptyIterator<T>() : cachedList.iterator());
		}

	}
	
}
