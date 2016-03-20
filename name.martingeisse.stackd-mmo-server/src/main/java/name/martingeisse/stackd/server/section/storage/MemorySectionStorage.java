/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section.storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.network.SectionDataId;

/**
 * Pure in-memory section storage.
 */
public final class MemorySectionStorage extends AbstractSectionStorage {

	/**
	 * the storageMap
	 */
	private final ConcurrentMap<SectionDataId, byte[]> storageMap = new ConcurrentHashMap<SectionDataId, byte[]>();

	/**
	 * Constructor.
	 * @param clusterSize the cluster size of sections
	 */
	public MemorySectionStorage(final ClusterSize clusterSize) {
		super(clusterSize);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#loadSectionRelatedObject(name.martingeisse.stackd.common.network.SectionDataId)
	 */
	@Override
	public byte[] loadSectionRelatedObject(final SectionDataId id) {
		return storageMap.get(id);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#loadSectionRelatedObjects(java.util.Collection)
	 */
	@Override
	public Map<SectionDataId, byte[]> loadSectionRelatedObjects(final Collection<? extends SectionDataId> ids) {
		final Map<SectionDataId, byte[]> result = new HashMap<SectionDataId, byte[]>();
		for (final SectionDataId id : ids) {
			result.put(id, storageMap.get(id));
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#saveSectionRelatedObject(name.martingeisse.stackd.common.network.SectionDataId, byte[])
	 */
	@Override
	public void saveSectionRelatedObject(final SectionDataId id, final byte[] data) {
		storageMap.put(id, data);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#deleteSectionRelatedObject(name.martingeisse.stackd.common.network.SectionDataId)
	 */
	@Override
	public void deleteSectionRelatedObject(final SectionDataId id) {
		storageMap.remove(id);
	}

}
