/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;


/**
 * This subclass of {@link ClassKeyedContainer} allows to be
 * sealed against modification.
 * @param <B> the base type of all contained objects
 */
public class SealableClassKeyedContainer<B> extends ClassKeyedContainer<B> {

	/**
	 * the sealed
	 */
	private boolean sealed;

	/**
	 * Constructor.
	 */
	public SealableClassKeyedContainer() {
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
	 * @see name.martingeisse.common.util.ClassKeyedContainer#set(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T extends B> T set(final Class<T> key, final T value) {
		ensureNotSealed();
		return super.set(key, value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.util.ClassKeyedContainer#remove(java.lang.Class)
	 */
	@Override
	public <T extends B> T remove(final Class<T> key) {
		ensureNotSealed();
		return super.remove(key);
	}

}
