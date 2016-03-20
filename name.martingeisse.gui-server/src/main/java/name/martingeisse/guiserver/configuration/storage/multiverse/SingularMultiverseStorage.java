/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.storage.multiverse;

import name.martingeisse.guiserver.configuration.storage.MultiverseStorage;
import name.martingeisse.guiserver.configuration.storage.UniverseStorage;

/**
 * This "multiverse" storage returns the same universe storage
 * for any ID.
 */
public final class SingularMultiverseStorage implements MultiverseStorage {

	/**
	 * the universeStorage
	 */
	private final UniverseStorage universeStorage;

	/**
	 * Constructor.
	 * @param universeStorage the universe storage
	 */
	public SingularMultiverseStorage(UniverseStorage universeStorage) {
		this.universeStorage = universeStorage;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.storage.MultiverseStorage#getUniverseStorage(java.lang.String)
	 */
	@Override
	public UniverseStorage getUniverseStorage(String id) {
		return universeStorage;
	}

}
