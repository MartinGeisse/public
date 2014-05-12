/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import name.martingeisse.stackd.common.cubes.Cubes;
import name.martingeisse.stackd.common.cubes.UniformCubes;
import name.martingeisse.stackd.common.network.SectionDataId;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * This cache keeps section-related data objects using their {@link SectionDataId}.
 * Each entry is the actual data, wrapped in a {@link SectionDataCacheEntry}.
 * 
 * TODO save on evict
 */
public final class SectionDataCache {

	/**
	 * the sectionWorkingSet
	 */
	private final SectionWorkingSet sectionWorkingSet;
	
	/**
	 * the cache
	 */
	private final LoadingCache<SectionDataId, SectionDataCacheEntry> cache;

	/**
	 * Constructor.
	 * @param sectionWorkingSet the working set that contains the cached objects
	 */
	public SectionDataCache(SectionWorkingSet sectionWorkingSet) {
		this.sectionWorkingSet = sectionWorkingSet;
		this.cache = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<SectionDataId, SectionDataCacheEntry>() {
			@Override
			public SectionDataCacheEntry load(SectionDataId sectionDataId) throws Exception {
				byte[] data = SectionDataCache.this.sectionWorkingSet.getStorage().loadSectionRelatedObject(sectionDataId);
				return createOrUnserialize(sectionDataId, data);
			}
		});
	}
	
	/**
	 * Getter method for the sectionWorkingSet.
	 * @return the sectionWorkingSet
	 */
	public SectionWorkingSet getSectionWorkingSet() {
		return sectionWorkingSet;
	}
	
	/**
	 * Removes all cached objects.
	 */
	public void clearCache() {
		cache.invalidateAll();
	}
	
	/**
	 * Returns a single object, loading it if necessary.
	 * @param sectionDataId the section data ID
	 * @return the section-related object
	 */
	public SectionDataCacheEntry get(SectionDataId sectionDataId) {
		try {
			return cache.get(sectionDataId);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Pre-caches the objects for the specified IDs.
	 * 
	 * @param sectionDataIds the section data IDs
	 */
	public final void precache(SectionDataId[] sectionDataIds) {
		
		// step through the IDs and only keep those for which the object isn't yet cached
		SectionDataId[] missingSectionDataIds = sectionDataIds.clone();
		int missingSectionDataIdCount = 0;
		for (SectionDataId sectionDataId : sectionDataIds) {
			if (cache.getIfPresent(sectionDataId) == null) {
				missingSectionDataIds[missingSectionDataIdCount] = sectionDataId;
				missingSectionDataIdCount++;
			}
		}
		if (missingSectionDataIdCount == 0) {
			return;
		}
		if (missingSectionDataIdCount < missingSectionDataIds.length) {
			missingSectionDataIds = Arrays.copyOfRange(missingSectionDataIds, 0, missingSectionDataIdCount);
		}
		
		// load the section-related objects and store them in the cache
		byte[][] datas = sectionWorkingSet.getStorage().loadSectionRelatedObjects(missingSectionDataIds);
		for (int i=0; i<missingSectionDataIds.length; i++) {
			SectionDataId sectionDataId = missingSectionDataIds[i];
			SectionDataCacheEntry entry = createOrUnserialize(sectionDataId, datas[i]);
			cache.asMap().putIfAbsent(sectionDataId, entry);
		}
		
	}

	/**
	 * Calls createDefault() or unserialize(), depending on whether the data
	 * argument is null.
	 */
	private SectionDataCacheEntry createOrUnserialize(SectionDataId sectionDataId, byte[] data) {
		if (data == null) {
			return createDefault(sectionDataId);
		} else {
			return unserializeForLoad(sectionDataId, data);
		}
	}
	
	/**
	 * Creates a default object for an ID that does not have that object in storage.
	 * 
	 * Note: there is currently no way to mark such objects as dirty before they have
	 * been placed into the cache, in case this function creates an object in a
	 * non-deterministic way. Just calling markModified() on the returned object
	 * is NOT correct as it creates a race condition with the worker thread that
	 * actually saves objects.
	 * --
	 * In case this gets implemented, the object should recognize that it was not added to
	 * the cache yet and handle this in markModified() (that is, add a task item when it
	 * later gets added). This should not require another method because the caller could
	 * still use markModified() accidentally which would be incorrect.
	 * 
	 * @param sectionDataId the section data ID
	 * @return the cache entry
	 */
	private SectionDataCacheEntry createDefault(SectionDataId sectionDataId) {
		switch (sectionDataId.getType()) {
		
		case DEFINITIVE:
			return new SectionCubesCacheEntry(getSectionWorkingSet(), sectionDataId, new UniformCubes((byte)0));
			
		case INTERACTIVE:
			return new InteractiveSectionImageCacheEntry(getSectionWorkingSet(), sectionDataId, null);
			
		case VIEW_LOD_0:
			throw new NotImplementedException();
			
		default:
			throw new IllegalArgumentException("invalid section data type in: " + sectionDataId);

		}
	}

	/**
	 * Creates a cached object from a serialized representation.
	 * 
	 * @param sectionDataId the section data ID
	 * @param data the loaded data
	 * @return the cache entry
	 */
	protected SectionDataCacheEntry unserializeForLoad(SectionDataId sectionDataId, byte[] data) {
		switch (sectionDataId.getType()) {
		
		case DEFINITIVE: {
			Cubes sectionCubes = Cubes.createFromCompressedData(getSectionWorkingSet().getClusterSize(), data);
			return new SectionCubesCacheEntry(getSectionWorkingSet(), sectionDataId, sectionCubes);
		}
			
		case INTERACTIVE:
			return new InteractiveSectionImageCacheEntry(getSectionWorkingSet(), sectionDataId, data);
			
		case VIEW_LOD_0:
			throw new NotImplementedException();
			
		default:
			throw new IllegalArgumentException("invalid section data type in: " + sectionDataId);

		}
	}

}
