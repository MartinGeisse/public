/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.storage;

/**
 * Base type for {@link StorageElement} and {@link StorageFolder}.
 */
public interface StorageFolderEntry {
	
	/**
	 * @return the local name for this entry
	 */
	public String getName() throws StorageException;
	
	/**
	 * @return the path for this entry
	 */
	public String getPath() throws StorageException;
	
}
