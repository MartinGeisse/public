/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.storage.http;

import java.io.InputStream;

import name.martingeisse.guiserver.configuration.storage.StorageElement;
import name.martingeisse.guiserver.configuration.storage.StorageException;

/**
 * Element storage implementation.
 */
public class HttpElement extends HttpFolderEntry implements StorageElement {

	/**
	 * Constructor.
	 * @param engine the engine
	 * @param httpPath the path used to fetch the entry using HTTP
	 * @param configurationPath the path to report to the remainder of the configuration system
	 */
	public HttpElement(HttpStorageEngine engine, String httpPath, String configurationPath) {
		super(engine, httpPath, configurationPath);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.storage.Element#open()
	 */
	@Override
	public InputStream open() throws StorageException {
		return getEngine().open(getHttpPath());
	}

}
