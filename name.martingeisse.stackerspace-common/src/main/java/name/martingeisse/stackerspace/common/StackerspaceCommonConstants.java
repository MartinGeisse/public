/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.common;

import name.martingeisse.stackd.common.geometry.ClusterSize;

/**
 * Common constants for client-side and server-side code.
 */
public class StackerspaceCommonConstants {

	/**
	 * The network port used by the client to connect to the server.
	 */
	public static final int NETWORK_PORT = 7260;

	/**
	 * the CLUSTER_SIZE
	 */
	public static final ClusterSize CLUSTER_SIZE = new ClusterSize(5);

}
