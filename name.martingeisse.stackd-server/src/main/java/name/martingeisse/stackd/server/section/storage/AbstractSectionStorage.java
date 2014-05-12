/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section.storage;

import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.network.SectionDataId;

/**
 * Base class for section storage implementations that are responsible
 * for actually storing section-related objects.
 */
public abstract class AbstractSectionStorage {

	/**
	 * the clusterSize
	 */
	private final ClusterSize clusterSize;

	/**
	 * Constructor.
	 * @param clusterSize the cluster-size of sections
	 */
	public AbstractSectionStorage(final ClusterSize clusterSize) {
		this.clusterSize = clusterSize;
	}

	/**
	 * Getter method for the clusterSize.
	 * @return the clusterSize
	 */
	public final ClusterSize getClusterSize() {
		return clusterSize;
	}

	/**
	 * Loads a single section-related object.
	 * 
	 * @param id the ID of the object to load
	 * @return the loaded object
	 */
	public final byte[] loadSectionRelatedObject(SectionDataId id) {
		return loadSectionRelatedObjects(new SectionDataId[] {id})[0];
	}

	/**
	 * Loads section-related objects for multiple sections, all using the same subkey.
	 * 
	 * @param ids the IDs of the objects to load
	 * @return the loaded objects
	 */
	public abstract byte[][] loadSectionRelatedObjects(SectionDataId[] ids);

	/**
	 * Saves a section-related object.
	 * 
	 * @param id the ID of the object to save
	 * @param data the object to store
	 */
	public abstract void saveSectionRelatedObject(SectionDataId id, byte[] data);

	/**
	 * Deletes a section-related object from storage.
	 * 
	 * @param id the ID of the object to delete
	 */
	public abstract void deleteSectionRelatedObject(SectionDataId id);

}
