/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.common.util.string.EmptyIterator;

/**
 * This class keeps track of the capabilities contributed by plugins.
 * 
 * Capabilities are recognized by a {@link CapabilityKey} which
 * also defines the type of object that represents the capability.
 * 
 * The capability registry can be sealed and will not allow any
 * further modification afterwards.
 * 
 * This class is meant for internal use by the capability system.
 * Public access is through {@link CapabilityKey} only.
 */
class CapabilityRegistry implements Serializable {

	/**
	 * the map
	 */
	private final HashMap<CapabilityKey<?>, List<?>> map;
	
	/**
	 * the sealed
	 */
	private boolean sealed;
	
	/**
	 * Constructor.
	 */
	CapabilityRegistry() {
		this.map = new HashMap<CapabilityKey<?>, List<?>>();
		this.sealed = false;
	}

	/**
	 * Seals this container.
	 */
	void seal() {
		this.sealed = true;
	}

	/**
	 * Checks whether this container is sealed, and if so, throws an
	 * {@link IllegalStateException}.
	 */
	private void ensureNotSealed() {
		if (sealed) {
			throw new IllegalStateException("container is already sealed");
		}
	}
	
	/**
	 * Returns the value list for the specified key. May return null
	 * instead of the empty list if no list has been created for the key yet.
	 * 
	 * @param key the key
	 * @return the value list stored for that key
	 */
	<C> List<C> getCachedList(CapabilityKey<C> key) {
		return GenericTypeUtil.unsafeCast(map.get(key));
	}
	
	/**
	 * Returns the list for the specified key, creating it if necessary.
	 * 
	 * @param key the key
	 * @return the list
	 */
	<C> List<C> getOrCreateList(CapabilityKey<C> key) {
		List<C> list = getCachedList(key);
		if (list == null) {
			list = new ArrayList<C>();
			map.put(key, list);
		}
		return list;
	}
	
	/**
	 * Adds a value using the specified key.
	 * @param key the key
	 * @param value the value to add
	 */
	<C> void add(CapabilityKey<C> key, C value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		ensureNotSealed();
		getOrCreateList(key).add(value);
	}

	/**
	 * Returns an {@link Iterator} for all values associated with the specified key.
	 * 
	 * @param key the key Class object
	 * @return an {@link Iterator} for the values for the specified key,
	 * backed by this container
	 */
	<C> Iterator<C> getIterator(CapabilityKey<C> key) {
		List<C> cachedList = getCachedList(key);
		return (cachedList == null ? new EmptyIterator<C>() : cachedList.iterator());
	}
	
}
