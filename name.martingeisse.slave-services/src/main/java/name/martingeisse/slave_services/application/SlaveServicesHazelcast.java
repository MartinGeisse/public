/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.application;

import com.hazelcast.core.HazelcastInstance;


/**
 * This class provides access to the shared frontend hazelcast
 * services.
 */
public final class SlaveServicesHazelcast {

	/**
	 * The hazelcast instance.
	 */
	public static HazelcastInstance instance;
	
	/**
	 * Prevent instantiation.
	 */
	private SlaveServicesHazelcast() {
	}
	
}