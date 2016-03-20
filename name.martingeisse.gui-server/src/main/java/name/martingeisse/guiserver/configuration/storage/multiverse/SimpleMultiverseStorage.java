/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.storage.multiverse;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.guiserver.configuration.storage.MultiverseStorage;
import name.martingeisse.guiserver.configuration.storage.UniverseStorage;

/**
 * Implements a pre-built multiverse storage completely in memory.
 */
public final class SimpleMultiverseStorage implements MultiverseStorage {

	/**
	 * the universeStorages
	 */
	private final Map<String, UniverseStorage> universeStorages = new HashMap<>();
	
	/**
	 * Adds the storage for a universe.
	 * 
	 * @param id the universe id
	 * @param storage the storage
	 */
	public void addUniverse(String id, UniverseStorage storage) {
		universeStorages.put(id, storage);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.storage.MultiverseStorage#getUniverseStorage(java.lang.String)
	 */
	@Override
	public UniverseStorage getUniverseStorage(String id) {
		return universeStorages.get(id);
	}

}
