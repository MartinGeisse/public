/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common;

import name.martingeisse.stackd.common.geometry.ClusterSize;

/**
 * Common constants for the Stack'd engine.
 */
public class StackdConstants {

	/**
	 * Whenever "detailed" coordinates are needed, fixed-point numbers
	 * are used that are represented using integers. Each cube, whose normal
	 * size is 1, is {@link #GEOMETRY_DETAIL_FACTOR} units wide in detail
	 * coordinates.
	 * 
	 * Detail coordinates are only used in exceptional cases, so whenever
	 * a method comment doesn't mention them, you can assume that the method
	 * uses normal coordinates (1 unit per cube). An example where detail
	 * coordinates are used is building the polygon meshes for "detailed"
	 * cube types. Using integer computations brings a surprising performance
	 * boost for them.
	 */
	public static final int GEOMETRY_DETAIL_FACTOR = 8;
	
	/**
	 * This value is the log2 of {@link #GEOMETRY_DETAIL_FACTOR} and can
	 * be used for bit shifting.
	 */
	public static final int GEOMETRY_DETAIL_SHIFT = 3;

	/**
	 * A {@link ClusterSize} for {@link #GEOMETRY_DETAIL_FACTOR}.
	 */
	public static final ClusterSize GEOMETRY_DETAIL_CLUSTER_SIZE = new ClusterSize(GEOMETRY_DETAIL_SHIFT);

	/**
	 * Prevent instantiation.
	 */
	private StackdConstants() {
	}
	
}
