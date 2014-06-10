/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.forum.application;

import com.hazelcast.core.HazelcastInstance;


/**
 * This class provides access to the shared frontend hazelcast
 * services.
 */
public final class ForumHazelcast {

	/**
	 * The hazelcast instance.
	 */
	public static HazelcastInstance instance;
	
	/**
	 * Prevent instantiation.
	 */
	private ForumHazelcast() {
	}
	
}
