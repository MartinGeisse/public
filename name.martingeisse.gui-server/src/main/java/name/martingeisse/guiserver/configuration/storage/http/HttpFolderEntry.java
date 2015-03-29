/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.storage.http;

import name.martingeisse.guiserver.configuration.storage.StorageFolderEntry;

/**
 * Folder entry storage implementation.
 */
public abstract class HttpFolderEntry implements StorageFolderEntry {

	/**
	 * the engine
	 */
	private final HttpStorageEngine engine;

	/**
	 * the httpPath
	 */
	private final String httpPath;

	/**
	 * the configurationPath
	 */
	private final String configurationPath;

	/**
	 * Constructor.
	 * @param engine the engine
	 * @param httpPath the path used to fetch the entry using HTTP
	 * @param configurationPath the path to report to the remainder of the configuration system
	 */
	public HttpFolderEntry(HttpStorageEngine engine, String httpPath, String configurationPath) {
		this.engine = engine;
		this.httpPath = httpPath;
		this.configurationPath = configurationPath;
	}

	/**
	 * Getter method for the engine.
	 * @return the engine
	 */
	public HttpStorageEngine getEngine() {
		return engine;
	}

	/**
	 * Getter method for the httpPath.
	 * @return the httpPath
	 */
	public String getHttpPath() {
		return httpPath;
	}

	/**
	 * Getter method for the configurationPath.
	 * @return the configurationPath
	 */
	public String getConfigurationPath() {
		return configurationPath;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.storage.FolderEntry#getPath()
	 */
	@Override
	public String getPath() {
		return getConfigurationPath();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.storage.StorageFolderEntry#getName()
	 */
	@Override
	public String getName() {
		String path = getPath();
		int index = path.lastIndexOf('/');
		return (index == -1 ? path : path.substring(index + 1));
	}

}
