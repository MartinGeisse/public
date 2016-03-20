/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.storage;

import java.io.IOException;
import java.io.InputStream;

/**
 * The storage for a configuration element.
 */
public interface StorageElement extends StorageFolderEntry {

	/**
	 * Opens this element storage for reading.
	 * 
	 * @return an input stream to read the element configuration
	 * @throws IOException on I/O errors
	 */
	public InputStream open() throws StorageException;

}
