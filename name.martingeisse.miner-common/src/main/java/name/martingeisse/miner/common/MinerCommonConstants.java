/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.common;

import name.martingeisse.stackd.common.geometry.ClusterSize;

/**
 * Common constants for client-side and server-side code.
 */
public class MinerCommonConstants {

	/**
	 * The network port used by the client to connect to the server.
	 */
	public static final int NETWORK_PORT = 7259;

	/**
	 * the BCRYPT_COST
	 */
	public static final int BCRYPT_COST = 12;
	
	/**
	 * the CLUSTER_SIZE
	 */
	public static final ClusterSize CLUSTER_SIZE = new ClusterSize(5);

}
