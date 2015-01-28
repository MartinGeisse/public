/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.engine.prepare;

import name.martingeisse.stackd.common.cubetype.CubeType;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.ClusterSize;

/**
 * A (u, v) plane of codes describing the adjacent plane of a neigbor section
 * while building the render model of the current section.
 */
public interface IWrapPlane {

	/**
	 * Returns the cube type of a cube in the wrap plane.
	 * 
	 * @param clusterSize the cluster size
	 * @param direction the direction that points from the current section to the neighbor section
	 * @param u the u coordinate of the cube
	 * @param v the v coordinate of the cube
	 * @param cubeTypes the cube types
	 * @return the cube type
	 */
	public CubeType getCubeType(ClusterSize clusterSize, AxisAlignedDirection direction, int u, int v, CubeType[] cubeTypes);
	
}
