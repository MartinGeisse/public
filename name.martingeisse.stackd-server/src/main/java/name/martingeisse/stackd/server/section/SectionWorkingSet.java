/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import name.martingeisse.stackd.common.cubes.Cubes;
import name.martingeisse.stackd.common.cubes.UniformCubes;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.network.SectionDataId;
import name.martingeisse.stackd.server.network.StackdServer;
import name.martingeisse.stackd.server.section.entry.InteractiveSectionImageCacheEntry;
import name.martingeisse.stackd.server.section.entry.SectionCubesCacheEntry;
import name.martingeisse.stackd.server.section.entry.SectionDataCacheEntry;
import name.martingeisse.stackd.server.section.storage.AbstractSectionStorage;
import org.apache.commons.collections.iterators.ArrayIterator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;

/**
 * This class is the front-end to section storage. It maintains a cache of recently
 * used section-related objects as well as an {@link AbstractSectionStorage} in the
 * background that handles actual storage of the sections.
 * 
 * Note that since objects are just cached, not permanently stored, fetching an
 * object may return a different object than before. This implies that code should
 * not hold on an old instance to avoid concurrent modification on two different
 * section objects, with save operations overwriting each other's changes.
 * 
 * The cache keeps section-related data objects using their {@link SectionDataId}.
 * Each entry is the actual data, wrapped in a {@link SectionDataCacheEntry}
 * subclass instance.
 * 
 * TODO save on evict
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
	 * the cache
	 */
	private final LoadingCache<SectionDataId, SectionDataCacheEntry> cache;

	/**
	 * Constructor.
	 * @param server the server that uses this storage
	 * @param storageFolder the storage folder to use for actually storing sections
	 */
	public SectionWorkingSet(final StackdServer<?> server, final AbstractSectionStorage storageFolder) {
		this.server = server;
		this.storage = storageFolder;
		this.cache = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<SectionDataId, SectionDataCacheEntry>() {

			@Override
			public SectionDataCacheEntry load(final SectionDataId sectionDataId) throws Exception {
				final byte[] data = storage.loadSectionRelatedObject(sectionDataId);
				return createOrUnserialize(sectionDataId, data);
			}

			@Override
			public Map<SectionDataId, SectionDataCacheEntry> loadAll(final Iterable<? extends SectionDataId> keys) throws Exception {
				final ArrayList<SectionDataId> ids = new ArrayList<SectionDataId>();
				for (final SectionDataId key : keys) {
					ids.add(key);
				}
				final Map<SectionDataId, byte[]> dataMap = storage.loadSectionRelatedObjects(ids);
				final Map<SectionDataId, SectionDataCacheEntry> result = new HashMap<SectionDataId, SectionDataCacheEntry>();
				for (SectionDataId key : keys) {
					result.put(key, createOrUnserialize(key, dataMap.get(key)));
				}
				return result;
			}

		});
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
	 * Removes all cached objects.
	 */
	public void clearCache() {
		cache.invalidateAll();
	}

	/**
	 * Returns a single object, loading it if necessary.
	 * 
	 * @param sectionDataId the section data ID
	 * @return the section-related object
	 */
	public SectionDataCacheEntry get(final SectionDataId sectionDataId) {
		try {
			return cache.get(sectionDataId);
		} catch (final ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns multiple objects, loading them if necessary.
	 * 
	 * @param sectionDataIds the section data IDs
	 * @return the section-related objects
	 */
	public ImmutableMap<SectionDataId, SectionDataCacheEntry> getAll(final SectionDataId... sectionDataIds) {
		try {
			return cache.getAll(new Iterable<SectionDataId>() {
				@Override
				@SuppressWarnings("unchecked")
				public Iterator<SectionDataId> iterator() {
					return new ArrayIterator(sectionDataIds);
				}
			});
		} catch (final ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns multiple objects, loading them if necessary.
	 * 
	 * @param sectionDataIds the section data IDs
	 * @return the section-related objects
	 */
	public ImmutableMap<SectionDataId, SectionDataCacheEntry> getAll(final Iterable<SectionDataId> sectionDataIds) {
		try {
			return cache.getAll(sectionDataIds);
		} catch (final ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Pre-caches the objects for the specified IDs.
	 * 
	 * @param sectionDataIds the section data IDs
	 */
	public final void precache(final SectionDataId[] sectionDataIds) {

		// step through the IDs and only keep those for which the object isn't yet cached
		final List<SectionDataId> missingIds = new ArrayList<>();
		for (final SectionDataId sectionDataId : sectionDataIds) {
			if (cache.getIfPresent(sectionDataId) == null) {
				missingIds.add(sectionDataId);
			}
		}
		if (missingIds.isEmpty()) {
			return;
		}

		// load the section-related objects and store them in the cache
		final Map<SectionDataId, byte[]> datas = storage.loadSectionRelatedObjects(missingIds);
		for (final Map.Entry<SectionDataId, byte[]> dataEntry : datas.entrySet()) {
			final SectionDataId sectionDataId = dataEntry.getKey();
			final SectionDataCacheEntry entry = createOrUnserialize(sectionDataId, dataEntry.getValue());
			cache.asMap().putIfAbsent(sectionDataId, entry);
		}

	}

	/**
	 * Calls createDefault() or unserialize(), depending on whether the data
	 * argument is null.
	 */
	private SectionDataCacheEntry createOrUnserialize(final SectionDataId sectionDataId, final byte[] data) {
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
	private SectionDataCacheEntry createDefault(final SectionDataId sectionDataId) {
		switch (sectionDataId.getType()) {

		case DEFINITIVE:
			return new SectionCubesCacheEntry(this, sectionDataId, new UniformCubes((byte)0));

		case INTERACTIVE:
			return new InteractiveSectionImageCacheEntry(this, sectionDataId, null);

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
	protected SectionDataCacheEntry unserializeForLoad(final SectionDataId sectionDataId, final byte[] data) {
		switch (sectionDataId.getType()) {

		case DEFINITIVE: {
			final Cubes sectionCubes = Cubes.createFromCompressedData(this.getClusterSize(), data);
			return new SectionCubesCacheEntry(this, sectionDataId, sectionCubes);
		}

		case INTERACTIVE:
			return new InteractiveSectionImageCacheEntry(this, sectionDataId, data);

		case VIEW_LOD_0:
			throw new NotImplementedException();

		default:
			throw new IllegalArgumentException("invalid section data type in: " + sectionDataId);

		}
	}

}
