/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.stackd.server.section.storage;

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
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#loadSectionRelatedObjects(name.martingeisse.stackd.common.network.SectionDataId[])
	 */
	@Override
	public byte[][] loadSectionRelatedObjects(SectionDataId[] ids) {
		byte[][] result = new byte[ids.length][];
		for (int i=0; i<result.length; i++) {
			result[i] = storageMap.get(ids[i]);
		}
		return result;
	}


	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#saveSectionRelatedObject(name.martingeisse.stackd.common.network.SectionDataId, byte[])
	 */
	@Override
	public void saveSectionRelatedObject(SectionDataId id, byte[] data) {
		storageMap.put(id, data);
	}


	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.storage.AbstractSectionStorage#deleteSectionRelatedObject(name.martingeisse.stackd.common.network.SectionDataId)
	 */
	@Override
	public void deleteSectionRelatedObject(SectionDataId id) {
		storageMap.remove(id);
	}

}
