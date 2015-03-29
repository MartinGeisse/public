/**
 * Copyright (c) 2013 Shopgate GmbH
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
