/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section;

import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.server.network.StackdServer;
import name.martingeisse.stackd.server.section.storage.AbstractSectionStorage;

/**
 * This class is the front-end to section storage. It maintains
 * a cache of recently used section-related objects as well as an
 * {@link AbstractSectionStorage} in the background that
 * handles actual storage of the sections.
 * 
 * Note that since objects are just cached, not permanently
 * stored, fetching an object may return a different object than
 * before. This implies that code should not hold on an
 * old instance to avoid concurrent modification on two
 * different section objects, with save operations overwriting
 * each other's changes.
 */
public final class SectionWorkingSet {

	/**
	 * the server
	 */
	private final StackdServer<?> server;

	/**
	 * the storage
	 */
	private final AbstractSectionStorage storage;

	/**
	 * the sectionDataCache
	 */
	private final SectionDataCache sectionDataCache;
	
	/**
	 * Constructor.
	 * @param server the server that uses this storage
	 * @param storageFolder the storage folder to use for actually storing sections
	 */
	public SectionWorkingSet(final StackdServer<?> server, final AbstractSectionStorage storageFolder) {
		this.server = server;
		this.storage = storageFolder;
		this.sectionDataCache = new SectionDataCache(this);
	}

	/**
	 * Getter method for the clusterSize.
	 * @return the clusterSize
	 */
	public ClusterSize getClusterSize() {
		return storage.getClusterSize();
	}

	/**
	 * Getter method for the server.
	 * @return the server
	 */
	public StackdServer<?> getServer() {
		return server;
	}

	/**
	 * Getter method for the storage.
	 * @return the storage
	 */
	public AbstractSectionStorage getStorage() {
		return storage;
	}

	/**
	 * Getter method for the sectionDataCache.
	 * @return the sectionDataCache
	 */
	public SectionDataCache getSectionDataCache() {
		return sectionDataCache;
	}

}
