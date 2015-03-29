/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.storage;


/**
 * The storage for a configuration folder.
 */
public interface StorageFolder extends StorageFolderEntry {

	/**
	 * Lists the entries in this folder.
	 * 
	 * @return the entries
	 */
	public StorageFolderEntry[] list() throws StorageException;
	
}
