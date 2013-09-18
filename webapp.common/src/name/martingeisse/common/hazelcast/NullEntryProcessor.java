/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.hazelcast;

import java.util.Map.Entry;

import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;

/**
 * An entry processor that does nothing. This is useful to make the
 * node which owns an entry pre-fetch that entry to itself from
 * the map loader.
 */
public final class NullEntryProcessor<K, V> implements EntryProcessor<K, V> {

	/**
	 * Shared instance of this class.
	 */
	public static final NullEntryProcessor<Object, Object> instance = new NullEntryProcessor<Object, Object>();
	
	/* (non-Javadoc)
	 * @see com.hazelcast.map.EntryProcessor#getBackupProcessor()
	 */
	@Override
	public EntryBackupProcessor<K, V> getBackupProcessor() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hazelcast.map.EntryProcessor#process(java.util.Map.Entry)
	 */
	@Override
	public Object process(Entry<K, V> entry) {
		return null;
	}
	
}
