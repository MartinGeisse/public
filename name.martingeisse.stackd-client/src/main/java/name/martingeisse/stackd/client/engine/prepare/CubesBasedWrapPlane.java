/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.engine.prepare;

import name.martingeisse.stackd.common.cubes.Cubes;
import name.martingeisse.stackd.common.cubetype.CubeType;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.ClusterSize;

/**
 * {@link IWrapPlane} implementation based on the {@link Cubes}
 * of the neighbor section. This class fetches the cubes lazily.
 */
public abstract class CubesBasedWrapPlane implements IWrapPlane {

	/**
	 * the cubes
	 */
	private Cubes cubes;

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.rendermodel.builder.ISectionRenderModelBuilderWrapPlane#getCubeType(name.martingeisse.stackd.common.geometry.ClusterSize, name.martingeisse.stackd.common.geometry.AxisAlignedDirection, int, int, name.martingeisse.stackd.common.cubetype.CubeType[])
	 */
	@Override
	public CubeType getCubeType(ClusterSize clusterSize, AxisAlignedDirection direction, int u, int v, CubeType[] cubeTypes) {
		if (cubes == null) {
			cubes = fetchCubes(clusterSize, direction);
		}
		final int plane = (direction.isNegative() ? clusterSize.getSize() - 1 : 0);
		final int x = direction.selectByAxis(plane, u, v);
		final int y = direction.selectByAxis(v, plane, u);
		final int z = direction.selectByAxis(u, v, plane);
		final int cubeTypeCode = (cubes.getCubeRelative(clusterSize, x, y, z) & 0xff);
		return cubeTypes[cubeTypeCode];
	}
	
	/**
	 * Fetches the {@link Cubes} for the neighbor section.
	 * @param clusterSize the cluster size
	 * @param direction the direction that points from the current section to the neighbor section
	 * @return the cubes
	 */
	protected abstract Cubes fetchCubes(ClusterSize clusterSize, AxisAlignedDirection direction);
	
}
