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
 * {@link IWrapPlane} implementation that reports all cubes as
 * nonsolid. This is a very simple implementation that creates too many visible faces
 * in most cases. This does not produce visible artifacts but slows down rendering.
 */
public final class EmptyWrapPlane implements IWrapPlane {

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.rendermodel.builder.ISectionRenderModelBuilderWrapPlane#getCubeType(name.martingeisse.stackd.common.geometry.ClusterSize, name.martingeisse.stackd.common.geometry.AxisAlignedDirection, int, int, name.martingeisse.stackd.common.cubetype.CubeType[])
	 */
	@Override
	public CubeType getCubeType(ClusterSize clusterSize, AxisAlignedDirection direction, int u, int v, CubeType[] cubeTypes) {
		return cubeTypes[0];
	}

}
