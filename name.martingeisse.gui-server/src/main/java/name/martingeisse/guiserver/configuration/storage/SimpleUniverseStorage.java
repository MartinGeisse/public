/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.storage;

/**
 * Simple {@link UniverseStorage} implementation that just wraps a
 * root folder.
 */
public final class SimpleUniverseStorage implements UniverseStorage {

	/**
	 * the rootFolder
	 */
	private final StorageFolder rootFolder;

	/**
	 * Constructor.
	 * @param rootFolder the root folder
	 */
	public SimpleUniverseStorage(StorageFolder rootFolder) {
		this.rootFolder = rootFolder;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.storage.UniverseStorage#getRootFolder()
	 */
	@Override
	public StorageFolder getRootFolder() {
		return rootFolder;
	}

}
