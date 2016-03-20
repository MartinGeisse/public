/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
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
