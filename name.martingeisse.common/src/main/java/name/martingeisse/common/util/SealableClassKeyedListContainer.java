/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;


/**
 * This subclass of {@link ClassKeyedListContainer} allows to be
 * sealed against modification.
 * 
 * Note: the list returned from this container cannot be sealed yet.
 * 
 * @param <B> the base type of all contained objects
 */
public class SealableClassKeyedListContainer<B> extends ClassKeyedListContainer<B> {

	/**
	 * the sealed
	 */
	private boolean sealed;

	/**
	 * Constructor.
	 */
	public SealableClassKeyedListContainer() {
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
	public void add(Class<? extends B> key, B value) {
		ensureNotSealed();
		super.add(key, value);
	}
	
}
