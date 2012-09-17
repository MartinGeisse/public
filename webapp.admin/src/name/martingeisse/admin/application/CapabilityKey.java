/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import java.util.Iterator;

/**
 * Identifies a type of capability that can be contributed by plugins
 * or by the application. For each key, any number of contributed
 * capabilities can be stored.
 * 
 * Capability keys are compared based on their identity. That is, two
 * capability keys can only be equal if they are the same object.
 * Therefore, capability keys are typically created and stored as
 * class constants.
 * 
 * @param <T> the type of contributed object
 */
public final class CapabilityKey<T> implements Iterable<T> {

	/**
	 * Constructor.
	 */
	public CapabilityKey() {
	}

	/**
	 * Adds a capability for this key.
	 * @param capability the capability to add
	 */
	public void add(T capability) {
		ApplicationConfiguration.get().getCapabilities().add(this, capability);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return ApplicationConfiguration.get().getCapabilities().getIterator(this);
	}
	
}
