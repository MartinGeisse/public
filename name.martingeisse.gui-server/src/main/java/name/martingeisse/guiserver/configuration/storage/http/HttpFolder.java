/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.storage.http;

import name.martingeisse.guiserver.configuration.storage.StorageException;
import name.martingeisse.guiserver.configuration.storage.StorageFolder;
import name.martingeisse.guiserver.configuration.storage.StorageFolderEntry;

/**
 * Folder storage implementation.
 */
public class HttpFolder extends HttpFolderEntry implements StorageFolder {

	/**
	 * Constructor.
	 * @param engine the engine
	 * @param httpPath the path used to fetch the entry using HTTP
	 * @param configurationPath the path to report to the remainder of the configuration system
	 */
	public HttpFolder(HttpStorageEngine engine, String httpPath, String configurationPath) {
		super(engine, httpPath, configurationPath);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.storage.Folder#list()
	 */
	@Override
	public StorageFolderEntry[] list() throws StorageException {
		return getEngine().list(getHttpPath());
	}

	/**
	 * Creates the root folder for the specified engine.
	 * @param engine the engine
	 * @return the root folder
	 */
	public static HttpFolder createRootFolder(HttpStorageEngine engine) {
		return new HttpFolder(engine, "/", "/");
	}

}
