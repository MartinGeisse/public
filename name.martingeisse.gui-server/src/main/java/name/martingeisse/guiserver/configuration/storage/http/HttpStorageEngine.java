/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.storage.http;

import java.io.InputStream;

import name.martingeisse.guiserver.configuration.storage.StorageException;

/**
 * Contains the actual implementation code for HTTP-based storages.
 */
public interface HttpStorageEngine {

	/**
	 * Opens the resource with the specified path.
	 * 
	 * @param path the path
	 * @return an input stream to read from the resource
	 */
	public InputStream open(String path) throws StorageException;
	
	/**
	 * Lists entries for the specified path.
	 * 
	 * @param path the path
	 * @return the entries
	 */
	public HttpFolderEntry[] list(String path) throws StorageException;
	
}
