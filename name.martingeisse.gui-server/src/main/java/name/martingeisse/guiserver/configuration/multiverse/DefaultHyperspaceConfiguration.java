/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.multiverse;

import name.martingeisse.guiserver.configuration.storage.MultiverseStorage;

/**
 * Default implementation of {@link HyperspaceConfiguration}.
 */
public class DefaultHyperspaceConfiguration implements HyperspaceConfiguration {

	/**
	 * the multiverseStorage
	 */
	private final MultiverseStorage multiverseStorage;

	/**
	 * Constructor.
	 * @param multiverseStorage the storage implementation for multiverse configurations
	 */
	public DefaultHyperspaceConfiguration(MultiverseStorage multiverseStorage) {
		if (multiverseStorage == null) {
			throw new IllegalArgumentException("multiverseStorage cannot be null");
		}
		this.multiverseStorage = multiverseStorage;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.HyperspaceConfiguration#getMultiverseStorage()
	 */
	@Override
	public MultiverseStorage getMultiverseStorage() {
		return multiverseStorage;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.multiverse.HyperspaceConfiguration#mapHostToUniverseId(java.lang.String)
	 */
	@Override
	public String mapHostToUniverseId(String host) {
		int index = host.indexOf('.');
		return (index == -1 ? host : host.substring(0, index));
	}

}
