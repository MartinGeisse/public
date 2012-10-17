/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.util.ParameterUtil;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This cache implementation uses lists as the publicly visibly value type.
 * Internally, it keeps blocks of elements of this list in an internal cache,
 * using a granularity (block size) specified at construction. This allows
 * to return public list windows efficiently.
 * 
 * This class provides a simple but unoptimized implementation for fetching
 * the lists for multiple keys at once, assuming that values are not typically
 * fetched this way.
 * 
 * @param <K> the cache key type
 * @param <E> the element type for cache value lists
 */
public class BlockGranularListRegion<K extends Serializable, E> implements ICacheWindowRegion<K, E> {

	/**
	 * the internalCache
	 */
	private final ICacheRegion<BlockSelector<K>, List<E>> internalCache;

	/**
	 * the blockSize
	 */
	private final int blockSize;

	/**
	 * Constructor.
	 * @param internalCache the internal cache that provides blocks
	 * @param blockSize the block size used for calculations. The internalCache should
	 * return blocks of this size. Specifically, for a fixed key and varying blockIndex,
	 * the internalCache should return:
	 * - blocks of size blockSize for the first N blockIndex values, where N is the
	 * number of full blocks that the internalCache can return,
	 * - optionally a block with 0 < size < blockSize, if the total number of values is
	 * not an even multiple of the blockSize,
	 * - empty blocks for higher blockIndex values.
	 */
	public BlockGranularListRegion(final ICacheRegion<BlockSelector<K>, List<E>> internalCache, final int blockSize) {
		this.internalCache = internalCache;
		this.blockSize = blockSize;
		
		// TODO: test this class
		throw new RuntimeException("test this class!");
	}

	/**
	 * Getter method for the internalCache.
	 * @return the internalCache
	 */
	public ICacheRegion<BlockSelector<K>, List<E>> getInternalCache() {
		return internalCache;
	}

	/**
	 * Getter method for the blockSize.
	 * @return the blockSize
	 */
	public int getBlockSize() {
		return blockSize;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.ICacheRegion#get(java.io.Serializable)
	 */
	@Override
	public List<E> get(final K key) throws UnsupportedOperationException {
		return get(key, 0, Integer.MAX_VALUE);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.ICacheRegion#get(java.lang.Iterable)
	 */
	@Override
	public List<List<E>> get(final Iterable<K> keys) throws UnsupportedOperationException {
		ParameterUtil.ensureNotNull(keys, "keys");
		final List<List<E>> result = new ArrayList<List<E>>();
		for (final K key : keys) {
			result.add(get(key));
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.cache.ICacheWindowRegion#get(java.io.Serializable, int, int)
	 */
	@Override
	public List<E> get(final K key, final int start, final int count) throws UnsupportedOperationException {
		
		// build block selectors
		int startBlockIndex = (start / blockSize);
		int stopBlockIndex = ((start + count + blockSize - 1) / blockSize);
		List<BlockSelector<K>> blockSelectors = new ArrayList<BlockSelector<K>>();
		for (int i=startBlockIndex; i<stopBlockIndex; i++) {
			blockSelectors.add(new BlockSelector<K>(key, i));
		}
		
		// fetch the blocks
		List<List<E>> blocks = internalCache.get(blockSelectors);
		List<E> result = new ArrayList<E>();
		
		// handle special cases: 0 or 1 blocks
		if (blocks.isEmpty()) {
			return result;
		} else if (blocks.size() == 1) {
			result.addAll(blocks.get(0));
			return result;
		}
		
		// now we have (partial, full*, partial) blocks
		List<E> firstBlock = blocks.remove(0);
		List<E> lastBlock = blocks.remove(blocks.size() - 1);
		
		// add the first block (partial)
		if ((start % blockSize) == 0) {
			result.addAll(firstBlock);
		} else {
			result.addAll(firstBlock.subList(start % blockSize, blockSize));
		}
		
		// add the middle blocks (full)
		for (List<E> block : blocks) {
			result.addAll(block);
		}
		
		// add the last block (partial)
		result.addAll(lastBlock.subList(0, (start + count) % blockSize));
		
		return result;
	}

	/**
	 * A simple pair of key (type K) and block index.
	 * @param <K> the cache key type
	 */
	public static final class BlockSelector<K extends Serializable> implements Serializable {

		/**
		 * the key
		 */
		private final K key;

		/**
		 * the blockIndex
		 */
		private final int blockIndex;

		/**
		 * Constructor.
		 * @param key the cache key
		 * @param blockIndex the block index
		 */
		public BlockSelector(final K key, final int blockIndex) {
			ParameterUtil.ensureNotNull(key, "key");
			if (blockIndex < 0) {
				throw new IllegalArgumentException("blockIndex is negative");
			}
			this.key = key;
			this.blockIndex = blockIndex;
		}

		/**
		 * Getter method for the key.
		 * @return the key
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Getter method for the blockIndex.
		 * @return the blockIndex
		 */
		public int getBlockIndex() {
			return blockIndex;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object otherObject) {
			if (otherObject instanceof BlockSelector<?>) {
				final BlockSelector<?> other = (BlockSelector<?>)otherObject;
				return key.equals(other.key) && blockIndex == other.blockIndex;
			} else {
				return false;
			}
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(key).append(blockIndex).toHashCode();
		}

	}

}
