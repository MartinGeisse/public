/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.collision;

import name.martingeisse.stackd.common.StackdConstants;
import name.martingeisse.stackd.common.cubetype.CubeType;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.geometry.RectangularRegion;
import name.martingeisse.stackd.common.geometry.SectionId;

/**
 * A collider for an array of cubes that represents a cube cluster. This
 * collider is backed by an array that stores a cube type index for each
 * cube in the array. An additional table of cube types looks up the
 * collider logic for individual types.
 * 
 * The collider's size is specified as a {@link ClusterSize} and its
 * position in space is specified by a {@link SectionId} measured in
 * cluster-size units.
 */
public final class CubeArrayClusterCollider implements IAxisAlignedCollider {

	/**
	 * the clusterSize
	 */
	private final ClusterSize clusterSize;

	/**
	 * the region
	 */
	private final RectangularRegion region;

	/**
	 * the cubes
	 */
	private final byte[] cubes;

	/**
	 * the cubeTypes
	 */
	private final CubeType[] cubeTypes;

	/**
	 * Constructor.
	 * @param clusterSize the cluster size of the section
	 * @param sectionId the section's ID
	 * @param cubes the array containing the cube type indices
	 * @param cubeTypes the cube type table
	 */
	public CubeArrayClusterCollider(ClusterSize clusterSize, SectionId sectionId, byte[] cubes, CubeType[] cubeTypes) {
		this.clusterSize = clusterSize;
		this.region = new RectangularRegion(sectionId.getX(), sectionId.getY(), sectionId.getZ()).multiply(clusterSize);
		this.cubes = cubes;
		this.cubeTypes = cubeTypes;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.collision.ICubeCollidingObject#getCurrentCollider()
	 */
	@Override
	public IAxisAlignedCollider getCurrentCollider() {
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.collision.ICubeCollider#collides(name.martingeisse.stackd.common.geometry.RectangularRegion)
	 */
	@Override
	public boolean collides(final RectangularRegion detailCoordinateRegion) {
		
		// map the region to a cube coordinate unit region
		final RectangularRegion cubeCoordinateRegion = detailCoordinateRegion.divideAndRoundToOuter(StackdConstants.GEOMETRY_DETAIL_CLUSTER_SIZE);

		// Compute the intersection of the argument region and this collider's region (in cube units).
		// Only that intersection must actually be checked. We quickly reject regions that do not
		// intersect the collider's region at all.
		final RectangularRegion cubeCoordinateIntersection = cubeCoordinateRegion.getIntersection(region);
		if (cubeCoordinateIntersection.isEmpty()) {
			return false;
		}

		// loop over the cubes
		final int detailShift = StackdConstants.GEOMETRY_DETAIL_SHIFT;
		final int x0 = region.getStartX();
		final int y0 = region.getStartY();
		final int z0 = region.getStartZ();
		final int size = clusterSize.getSize();
		final int size2 = clusterSize.getSquaredSize();
		for (int x = cubeCoordinateIntersection.getStartX(); x < cubeCoordinateIntersection.getEndX(); x++) {
			final int dx = x - x0;
			if (dx < 0 || dx >= size) {
				continue;
			}
			final int dxBase = dx * size2;
			final int detailDeltaStartX = (detailCoordinateRegion.getStartX() - (x << detailShift));
			final int detailDeltaEndX = (detailCoordinateRegion.getEndX() - (x << detailShift));
			for (int y = cubeCoordinateIntersection.getStartY(); y < cubeCoordinateIntersection.getEndY(); y++) {
				final int dy = y - y0;
				if (dy < 0 || dy >= size) {
					continue;
				}
				final int dxdyBase = dxBase + dy * size;
				final int detailDeltaStartY = (detailCoordinateRegion.getStartY() - (y << detailShift));
				final int detailDeltaEndY = (detailCoordinateRegion.getEndY() - (y << detailShift));
				for (int z = cubeCoordinateIntersection.getStartZ(); z < cubeCoordinateIntersection.getEndZ(); z++) {
					final int dz = z - z0;
					if (dz < 0 || dz >= size) {
						continue;
					}
					int cubeTypeIndex = (cubes[dxdyBase + dz] & 0xff);
					CubeType cubeType = cubeTypes[cubeTypeIndex];
					final int detailDeltaStartZ = (detailCoordinateRegion.getStartZ() - (z << detailShift));
					final int detailDeltaEndZ = (detailCoordinateRegion.getEndZ() - (z << detailShift));
					if (cubeType.collidesWithRegion(detailDeltaStartX, detailDeltaStartY, detailDeltaStartZ, detailDeltaEndX, detailDeltaEndY, detailDeltaEndZ)) {
						return true;
					}
				}
			}
		}
		return false;

	}

}
