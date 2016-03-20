/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.storage;

/**
 * Represents the storage for a configuration universe.
 */
public interface UniverseStorage {

	/**
	 * Returns the root folder.
	 * @return the root folder
	 */
	public StorageFolder getRootFolder() throws StorageException;
	
}
