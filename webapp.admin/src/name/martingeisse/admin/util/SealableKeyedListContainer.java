/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util;

import name.martingeisse.common.util.KeyedListContainer;

/**
 * This subclass of {@link KeyedListContainer} allows to be
 * sealed against modification.
 * 
 * Note: the list returned from this container cannot be sealed yet.
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public class SealableKeyedListContainer<K, V> extends KeyedListContainer<K, V> {

	/**
	 * the sealed
	 */
	private boolean sealed;

	/**
	 * Constructor.
	 */
	public SealableKeyedListContainer() {
		this.sealed = false;
	}

	/**
	 * Seals this container.
	 */
	public void seal() {
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

	/* (non-Javadoc)
	 * @see name.martingeisse.common.util.KeyedListContainer#add(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void add(K key, V value) {
		ensureNotSealed();
		super.add(key, value);
	}
	
}
