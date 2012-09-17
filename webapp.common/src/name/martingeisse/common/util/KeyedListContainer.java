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
 * This class wraps a hash map that has lists as values. For each
 * key, values can be added and later an iterator for all values
 * can be obtained. Creating the lists happens automatically when
 * the first value for a key is added.
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public class KeyedListContainer<K, V> implements Serializable {

	/**
	 * the map
	 */
	private HashMap<K, List<V>> map;
	
	/**
	 * Constructor.
	 */
	public KeyedListContainer() {
		this.map = new HashMap<K, List<V>>();
	}
	
	/**
	 * Returns the value list for the specified key. May return null
	 * instead of the empty list if no list has been created for the key yet.
	 * @param key the key
	 * @return the value list stored for that key
	 */
	public List<V> getCachedList(K key) {
		return map.get(key);
	}
	
	/**
	 * Returns the list for the specified key, creating it if necessary.
	 * @param key the key
	 * @return the list
	 */
	public List<V> getOrCreateList(K key) {
		List<V> list = map.get(key);
		if (list == null) {
			list = new ArrayList<V>();
			map.put(key, list);
		}
		return list;
	}
	
	/**
	 * Adds a value using the specified key.
	 * @param key the key
	 * @param value the value to add
	 */
	public void add(K key, V value) {
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
	 * @param key the key Class object
	 * @return an {@link Iterable} for the values for the specified key,
	 * backed by this container
	 */
	public <S extends V> Iterable<S> getIterable(K key) {
		return new MyIterable<S>(key);
	}
	
	/**
	 * Implementation of {@link Iterable} that looks up the value list
	 * at the time an {@link Iterator} is created from it.
	 */
	private class MyIterable<S extends V> implements Iterable<S> {
		
		/**
		 * the key
		 */
		private K key;

		/**
		 * Constructor.
		 * @param key the key to iterate values for
		 */
		public MyIterable(K key) {
			this.key = key;
		}

		/* (non-Javadoc)
		 * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<S> iterator() {
			List<V> cachedList = getCachedList(key);
			return (cachedList == null ? new EmptyIterator<S>() : GenericTypeUtil.<Iterator<S>> unsafeCast(cachedList.iterator()));
		}

	}
	
}
