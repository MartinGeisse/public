/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.cubetype;

import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;

/**
 * The special type for an empty cube. This type usually uses type index 0.
 */
public class EmptyCubeType extends CubeType {

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#obscuresNeighbor(name.martingeisse.stackd.common.geometry.AxisAlignedDirection)
	 */
	@Override
	public final boolean obscuresNeighbor(AxisAlignedDirection direction) {
		return false;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#blocksMovementToNeighbor(name.martingeisse.stackd.common.geometry.AxisAlignedDirection)
	 */
	@Override
	public boolean blocksMovementToNeighbor(AxisAlignedDirection direction) {
		return false;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#getCubeFaceTextureIndex(int)
	 */
	@Override
	public final int getCubeFaceTextureIndex(int directionOrdinal) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#collidesWithRegion(int, int, int, int, int, int)
	 */
	@Override
	public boolean collidesWithRegion(int startX, int startY, int startZ, int endX, int endY, int endZ) {
		return false;
	}
	
}
