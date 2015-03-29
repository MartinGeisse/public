/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.storage;

/**
 * Represents the storage for the configuration multiverse.
 */
public interface MultiverseStorage {

	/**
	 * Returns the configuration universe storage for the
	 * specified universe ID.
	 * 
	 * @param id the universe id
	 * @return the universe storage
	 */
	public UniverseStorage getUniverseStorage(String id);
	
}
